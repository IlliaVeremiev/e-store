package ua.illia.estore.configuration.security.oauth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import ua.illia.estore.configuration.camunda.auth.CamundaServletRequestWrapper;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.configuration.security.CustomerUserDetails;
import ua.illia.estore.configuration.security.EmployeeUserDetails;
import ua.illia.estore.configuration.security.Headers;
import ua.illia.estore.configuration.security.helpers.CookieHelper;
import ua.illia.estore.model.security.Customer;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.services.management.CustomerService;
import ua.illia.estore.services.management.EmployeeService;
import ua.illia.estore.services.management.GroupService;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Selects {@value #} cookie value from request
 * and inject related user details to spring security context holder
 */
public class CustomOAuthTokenAuthenticationFilter implements Filter {

    public static final String TARGET_APP_HEADER_NAME = "Target-App";
    public static final String BACK_OFFICE_TARGET_APP = "Back Office";

    private final UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();

    private CustomerService customerService;

    private EmployeeService employeeService;

    private PasswordEncoder publicTokenHashEncoder;

    private OAuthService authService;

    private GroupService groupService;

    public CustomOAuthTokenAuthenticationFilter(CustomerService customerService, EmployeeService employeeService, PasswordEncoder publicTokenHashEncoder, OAuthService authService, GroupService groupService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.publicTokenHashEncoder = publicTokenHashEncoder;
        this.authService = authService;
        this.groupService = groupService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        /**
         * This condition need to split customer and employee authorization.
         * When Basic Authorization is passed, token authorization will be skipped.
         * TODO: after basic authorization will be fully disabled, this condition can be removed
         */
        if (httpRequest.getHeader("Authorization") != null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        /**
         * Some hack to avoid camunda authentication. In case employee authorized for camunda endpoints,
         * creates request wrapper that returns fake session, that returns fake camunda authentication,
         * mapped with global app authentication
         */
        if (httpRequest.getRequestURI().startsWith("/camunda") || httpRequest.getRequestURI().startsWith("/engine-rest")) {
            performCamundaAuthorization(httpRequest, httpResponse, filterChain);
            return;
        }
        try {
            if (StringUtils.equals(httpRequest.getHeader(TARGET_APP_HEADER_NAME), BACK_OFFICE_TARGET_APP)) {
                getCookies(httpRequest)
                        .flatMap(c -> getSessionCookie(c, OAuthService.E_SESSION_COOKIE_NAME))
                        .ifPresent(this::setEmployeeDetailsFromCookie);
            } else {
                getCookies(httpRequest)
                        .flatMap(c -> getSessionCookie(c, OAuthService.SESSION_COOKIE_NAME))
                        .ifPresent(this::setUserDetailsFromCookie);
            }
        } catch (Exception e) {
            httpResponse.addHeader(HttpHeaders.SET_COOKIE, CookieHelper.generateExpiredCookie(OAuthService.SESSION_COOKIE_NAME, httpRequest));
            httpResponse.addHeader(HttpHeaders.SET_COOKIE, CookieHelper.generateExpiredCookie(OAuthService.E_SESSION_COOKIE_NAME, httpRequest));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void performCamundaAuthorization(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = getCookies(httpRequest)
                .flatMap(c -> getSessionCookie(c, OAuthService.E_SESSION_COOKIE_NAME))
                .orElse(null);
        if (cookie == null) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }
        setEmployeeDetailsFromCookie(cookie);
        filterChain.doFilter(new CamundaServletRequestWrapper(httpRequest), httpResponse);
        httpResponse.setHeader(Headers.ACCESS_CONTROL_ALLOW_ORIGIN, httpRequest.getHeader(HttpHeaders.ORIGIN));
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    private Optional<Cookie[]> getCookies(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies());
    }

    private Optional<Cookie> getSessionCookie(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies).filter(c -> c.getName().equals(cookieName)).findFirst();
    }

    private void setUserDetailsFromCookie(Cookie cookie) {
        OAuthService.LoginData loginData = authService.getDataFromToken(cookie.getValue());
        Authentication authentication = authenticate(new CustomTokenAuthentication(loginData.getUid(), loginData.getPasswordHash()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void setEmployeeDetailsFromCookie(Cookie cookie) {
        OAuthService.LoginData loginData = authService.getDataFromToken(cookie.getValue());
        Authentication authentication = authenticateEmployee(new CustomTokenAuthentication(loginData.getUid(), loginData.getPasswordHash()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Authentication authenticate(CustomTokenAuthentication authentication) throws AuthenticationException {
        String username = authentication.getUid();
        CustomerUserDetails userDetails = retrieveUser(username);
        Assert.notNull(userDetails, "retrieveUser returned null - a violation of the interface contract");
        preAuthenticationChecks.check(userDetails);
        if (!publicTokenHashEncoder.matches(userDetails.getUser().getAuthorizationToken(), authentication.getPasswordHash())) {
            throw new BadCredentialsException("Incorrect password token");
        }
        if (!userDetails.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired");
        }
        authentication.setPrincipal(userDetails);
        return authentication;
    }

    public Authentication authenticateEmployee(CustomTokenAuthentication authentication) throws AuthenticationException {
        String username = authentication.getUid();
        EmployeeUserDetails userDetails = retrieveEmployee(username);
        Assert.notNull(userDetails, "retrieveUser returned null - a violation of the interface contract");
        preAuthenticationChecks.check(userDetails);
        if (!publicTokenHashEncoder.matches(userDetails.getUser().getAuthorizationToken(), authentication.getPasswordHash())) {
            throw new BadCredentialsException("Incorrect password token");
        }
        if (!userDetails.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired");
        }
        authentication.setPrincipal(userDetails);
        return authentication;
    }

    private CustomerUserDetails retrieveUser(String uid) {
        try {
            Customer customer = customerService.getByUid(uid);
            return new CustomerUserDetails(customer);
        } catch (NotFoundException e) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        } catch (UsernameNotFoundException | InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    private EmployeeUserDetails retrieveEmployee(String uid) {
        try {
            Employee employee = employeeService.getByUid(uid);
            return new EmployeeUserDetails(employee, groupService.getEmployeeAuthorities(employee));
        } catch (NotFoundException e) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        } catch (UsernameNotFoundException | InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    private static class DefaultPreAuthenticationChecks implements UserDetailsChecker {

        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                throw new LockedException("Failed to authenticate since user account is locked");
            }
            if (!user.isEnabled()) {
                throw new DisabledException("Failed to authenticate since user account is disabled");
            }
            if (!user.isAccountNonExpired()) {
                throw new AccountExpiredException("Failed to authenticate since user account has expired");
            }
        }
    }
}