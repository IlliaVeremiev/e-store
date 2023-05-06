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
import ua.illia.estore.configuration.security.CustomerUserDetails;
import ua.illia.estore.configuration.security.helpers.CookieHelper;
import ua.illia.estore.configuration.security.oauth.OAuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/authentication")
public class CustomerLoginController {

    @Autowired
    private DaoAuthenticationProvider customerAuthenticationProvider;

    @Autowired
    private OAuthService authService;

    @PostMapping
    public void login(@RequestBody LoginData loginData, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = customerAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginData.username, loginData.password));
        CustomerUserDetails customerUserDetails = (CustomerUserDetails) authentication.getPrincipal();
        String value = authService.generateUserToken(customerUserDetails.getUser());
        response.addHeader(HttpHeaders.SET_COOKIE, CookieHelper.generateCookie(OAuthService.SESSION_COOKIE_NAME, value, Duration.ofDays(30), true, loginData.rememberMe, request));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, CookieHelper.generateExpiredCookie(OAuthService.SESSION_COOKIE_NAME, request));
    }

    @Data
    static class LoginData {
        private String username;
        private String password;
        private boolean rememberMe;
    }
}
