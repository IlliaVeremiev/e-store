package ua.illia.estore.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Env {
        public static final String PROD = "prod";
        public static final String DEMO = "demo";
        public static final String DEV = "dev";
    }
}
