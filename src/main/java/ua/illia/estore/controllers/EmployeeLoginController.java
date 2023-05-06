package ua.illia.estore.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.configuration.security.EmployeeUserDetails;
import ua.illia.estore.configuration.security.helpers.CookieHelper;
import ua.illia.estore.configuration.security.oauth.OAuthService;
import ua.illia.estore.converters.impl.EmployeeConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/office")
public class EmployeeLoginController {

    @Autowired
    private EmployeeConverter employeeConverter;

    @Autowired
    private DaoAuthenticationProvider employeeAuthenticationProvider;

    @Autowired
    private OAuthService authService;

    @PostMapping("/login")
    public void login(@RequestBody LoginData loginData, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = employeeAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginData.username, loginData.password));
        EmployeeUserDetails customerUserDetails = (EmployeeUserDetails) authentication.getPrincipal();
        String value = authService.generateUserToken(customerUserDetails.getUser());
        response.addHeader(HttpHeaders.SET_COOKIE, CookieHelper.generateCookie(OAuthService.E_SESSION_COOKIE_NAME, value, Duration.ofDays(30), true, true, request));
    }

    @Data
    static class LoginData {
        private String username;
        private String password;
    }
}
