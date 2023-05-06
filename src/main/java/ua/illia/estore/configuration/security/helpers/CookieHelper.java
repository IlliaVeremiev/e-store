package ua.illia.estore.configuration.security.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Optional;

import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieHelper {
    public static Optional<String> retrieve(Cookie[] cookies, @NonNull String name) {
        if (isNull(cookies)) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                return Optional.ofNullable(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    /**
     * TODO when looking for this, found information about condition below. Need to make some investigation about this, because not sure really need to do this
     * if (!"localhost".equals(COOKIE_DOMAIN)) { // https://stackoverflow.com/a/1188145
     *             cookie.setDomain(COOKIE_DOMAIN);
     *         }
     */

    public static String generateCookie(@NonNull String name, String value, @NonNull Duration maxAge, boolean secure, boolean rememberMe, HttpServletRequest request) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)//TODO in prod should be true
                .path("/");
        if (rememberMe) {
            builder.maxAge(maxAge);
        }
        return builder.build().toString();
    }

    public static String generateExpiredCookie(@NonNull String name, HttpServletRequest request) {
        return generateCookie(name, null, Duration.ZERO, true, true, request);
    }
}
