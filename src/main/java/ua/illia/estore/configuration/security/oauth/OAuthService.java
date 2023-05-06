package ua.illia.estore.configuration.security.oauth;

import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.configuration.security.Headers;
import ua.illia.estore.configuration.security.helpers.AuthenticationHelper;
import ua.illia.estore.configuration.security.helpers.CookieHelper;
import ua.illia.estore.dto.customer.CustomerOauthCreateForm;
import ua.illia.estore.model.security.Customer;
import ua.illia.estore.model.security.Employee;
import ua.illia.estore.services.management.CustomerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class OAuthService {

    public static final String AUTHORIZATION_BASE_URL = "/oauth2/authorization";
    public static final String CALLBACK_BASE_URL = "/oauth2/callback";
    public static final String OAUTH_COOKIE_NAME = "OAUTH";
    public static final String SESSION_COOKIE_NAME = "X-Authentication";
    public static final String E_SESSION_COOKIE_NAME = "X-EAuthentication";

    @Autowired
    private BytesEncryptor bytesEncryptor;
    @Autowired
    private PasswordEncoder publicTokenHashEncoder;
    @Autowired
    private CustomerService customerService;

    public void oauthSuccessCallback(OAuth2AuthorizedClient client, Authentication authentication) {
        // TODO can grab the access + refresh tokens as well via the "client"
        Customer customer = findOrRegisterAccount(client, authentication);
        String encrypted = generateUserToken(customer);
        AuthenticationHelper.attachAccountId(authentication, encrypted);
    }

    private Customer findOrRegisterAccount(OAuth2AuthorizedClient client, Authentication authentication) {
        CustomerOauthCreateForm form = new CustomerOauthCreateForm();

        if ("google".equals(client.getClientRegistration().getRegistrationId())) {
            if (authentication.getPrincipal() instanceof DefaultOidcUser) {
                DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
                String email = principal.getAttribute("email");
                try {
                    return customerService.getByEmail(email);
                } catch (NotFoundException ex) {
                    form.setEmail(email);
                    form.setLocale(principal.getAttribute("locale"));
                    form.setPicture(principal.getAttribute("picture"));
                    form.setFirstName(principal.getAttribute("given_name"));
                    form.setLastName(principal.getAttribute("family_name"));
                }
            }
        } else if ("facebook".equals(client.getClientRegistration().getRegistrationId())) {
            if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
                DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
                String email = principal.getAttribute("email");
                try {
                    return customerService.getByEmail(email);
                } catch (NotFoundException ex) {
                    form.setEmail(email);
                    String fullName = principal.getAttribute("name");
                    int indexOfSeparator = StringUtils.indexOf(fullName, ' ');
                    String firstName = StringUtils.substring(fullName, 0, indexOfSeparator);
                    String lastName = StringUtils.substring(fullName, indexOfSeparator + 1);
                    form.setFirstName(firstName);
                    form.setLastName(lastName);
                }
            }
        } else {
            throw new NotImplementedException("authorization for client " + client.getPrincipalName() + " not defined");
        }
        form.setUid(UUID.randomUUID().toString());
        return customerService.create(form);
    }

    public void oauthSuccessResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String accountId = AuthenticationHelper.retrieveAccountId(authentication);
        response.addHeader(HttpHeaders.SET_COOKIE, CookieHelper.generateExpiredCookie(OAUTH_COOKIE_NAME, request));
        response.addHeader(HttpHeaders.SET_COOKIE, CookieHelper.generateCookie(SESSION_COOKIE_NAME, accountId, Duration.ofDays(1), true, true, request));
        response.setHeader(Headers.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
        response.setHeader(Headers.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        Optional<String> redirectLink = CookieHelper.retrieve(request.getCookies(), "Oauth-Redirect-Link");
        response.sendRedirect(redirectLink.get());//TODO wrap into object at least response.getWriter().write("{ \"status\": \"success\", \"link\":\"...\" }")
    }

    /**
     * Temporary solution that just returns failure json to client, but it can be used in future tu handle authorization errors.
     */
    public void oauthFailureResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(HttpHeaders.SET_COOKIE, CookieHelper.generateExpiredCookie(OAUTH_COOKIE_NAME, request));
        response.setHeader(Headers.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
        response.setHeader(Headers.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.getWriter().write("{ \"status\": \"failure\" }");
    }

    public String generateUserToken(Customer customer) {
        return generateUserToken(customer.getUid(), customer.getAuthorizationToken());
    }

    public String generateUserToken(Employee employee) {
        return generateUserToken(employee.getUid(), employee.getAuthorizationToken());
    }

    public String generateUserToken(String uidStr, String authorizationToken) {
        byte[] uid = Base64.encodeBase64(bytesEncryptor.encrypt(uidStr.getBytes(StandardCharsets.UTF_8)));
        byte[] delimiter = ":".getBytes(StandardCharsets.UTF_8);
        byte[] password = publicTokenHashEncoder.encode(authorizationToken).getBytes(StandardCharsets.UTF_8);
        byte[] token = new byte[uid.length + 1 + password.length];
        System.arraycopy(uid, 0, token, 0, uid.length);
        System.arraycopy(delimiter, 0, token, uid.length, 1);
        System.arraycopy(password, 0, token, uid.length + 1, password.length);
        return Base64.encodeBase64String(token);
    }

    public LoginData getDataFromToken(String token) {
        byte[] tokenBytes = Base64.decodeBase64(token);
        byte delimiter = ":".getBytes(StandardCharsets.UTF_8)[0];
        int index = Arrays.asList(ArrayUtils.toObject(tokenBytes)).indexOf(delimiter);
        byte[] uidEncoded = ArrayUtils.subarray(tokenBytes, 0, index);
        byte[] passwordEncoded = ArrayUtils.subarray(tokenBytes, index + 1, tokenBytes.length);
        String uid = new String(bytesEncryptor.decrypt(Base64.decodeBase64(uidEncoded)), StandardCharsets.UTF_8);
        String passwordHash = new String(passwordEncoded, StandardCharsets.UTF_8);
        return new LoginData(uid, passwordHash);
    }

    @SneakyThrows
    public void oauthRedirectResponse(HttpServletRequest request, HttpServletResponse response, String url) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(Headers.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
        response.setHeader(Headers.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.getWriter().write(String.format("{ \"redirectUrl\": \"%s\" }", url));
    }

    @Data
    public static class LoginData {
        private String uid;
        private String passwordHash;

        public LoginData(String uid, String passwordHash) {
            this.uid = uid;
            this.passwordHash = passwordHash;
        }
    }
}
