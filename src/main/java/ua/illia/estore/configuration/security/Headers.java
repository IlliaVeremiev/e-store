package ua.illia.estore.configuration.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Headers {
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
}
