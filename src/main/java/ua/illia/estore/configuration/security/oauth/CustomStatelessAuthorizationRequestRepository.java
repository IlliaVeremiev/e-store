package ua.illia.estore.configuration.security.oauth;

import org.apache.http.util.Asserts;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import ua.illia.estore.configuration.security.helpers.CookieHelper;
import ua.illia.estore.configuration.security.helpers.EncryptionHelper;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Base64;

@Component
public class CustomStatelessAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final Duration OAUTH_COOKIE_EXPIRY = Duration.ofMinutes(5);
    private static final Base64.Encoder B64E = Base64.getEncoder();
    private static final Base64.Decoder B64D = Base64.getDecoder();

    private final SecretKey encryptionKey;

    public CustomStatelessAuthorizationRequestRepository() {
        this.encryptionKey = EncryptionHelper.generateKey();
    }

    /**
     * A static salt is OK for these short lived session cookies
     */
    public CustomStatelessAuthorizationRequestRepository(@NonNull char[] encryptionPassword) {
        byte[] salt = {-122, 45, 24, 19 - 124, 2, 4, 21};
        this.encryptionKey = EncryptionHelper.generateKey(encryptionPassword, salt);
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return this.retrieveCookie(request);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            this.removeCookie(request, response);
            return;
        }
        this.attachCookie(request, response, authorizationRequest, request.getHeader(HttpHeaders.ORIGIN));
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.retrieveCookie(request);
    }

    private OAuth2AuthorizationRequest retrieveCookie(HttpServletRequest request) {
        return CookieHelper.retrieve(request.getCookies(), OAuthService.OAUTH_COOKIE_NAME)
                .map(this::decrypt)
                .orElse(null);
    }

    private void attachCookie(HttpServletRequest request, HttpServletResponse response, OAuth2AuthorizationRequest value, String origin) {
        String cookie = CookieHelper.generateCookie(OAuthService.OAUTH_COOKIE_NAME, this.encrypt(value), OAUTH_COOKIE_EXPIRY, true, true, request);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie);
        response.addHeader(HttpHeaders.SET_COOKIE, CookieHelper.generateCookie("Oauth-Redirect-Link", origin, OAUTH_COOKIE_EXPIRY, true, true, request));
    }

    private void removeCookie(HttpServletRequest request, HttpServletResponse response) {
        String expiredCookie = CookieHelper.generateExpiredCookie(OAuthService.OAUTH_COOKIE_NAME, request);
        response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie);
    }

    private String encrypt(OAuth2AuthorizationRequest authorizationRequest) {
        Asserts.notNull(this.encryptionKey, "encryptionKey.null");
        byte[] bytes = SerializationUtils.serialize(authorizationRequest);
        byte[] encryptedBytes = EncryptionHelper.encrypt(this.encryptionKey, bytes);
        return B64E.encodeToString(encryptedBytes);
    }

    private OAuth2AuthorizationRequest decrypt(String encrypted) {
        Asserts.notNull(this.encryptionKey, "encryptionKey.null");
        byte[] encryptedBytes = B64D.decode(encrypted);
        byte[] bytes = EncryptionHelper.decrypt(this.encryptionKey, encryptedBytes);
        return (OAuth2AuthorizationRequest) SerializationUtils.deserialize(bytes);
    }
}
