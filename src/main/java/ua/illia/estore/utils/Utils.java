package ua.illia.estore.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static <E> E valueOrDefault(E value, E orElse) {
        if (value == null) {
            return orElse;
        }
        return value;
    }
}
