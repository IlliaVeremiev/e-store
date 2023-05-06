package ua.illia.estore.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalizationUtil {

    public static final String DEFAULT_LANGUAGE_CODE = "uk";

    public static String getLocalized(Map<String, String> localized, String otherName) {
        if (localized == null) {
            return otherName;
        }
        Locale locale = LocaleContextHolder.getLocale();
        String languageCode = locale.getLanguage();
        if (localized.containsKey(languageCode)) {
            return localized.get(languageCode);
        }
        if (localized.containsKey(DEFAULT_LANGUAGE_CODE)) {
            return localized.get(DEFAULT_LANGUAGE_CODE);
        }
        return otherName;
    }

    public static String getLocalized(Map<String, String> localized) {
        if (localized == null) {
            return "";
        }
        Locale locale = LocaleContextHolder.getLocale();
        String languageCode = locale.getLanguage();
        if (localized.containsKey(languageCode)) {
            return localized.get(languageCode);
        }
        if (localized.containsKey(DEFAULT_LANGUAGE_CODE)) {
            return localized.get(DEFAULT_LANGUAGE_CODE);
        }
        if (localized.size() > 0) {
            return localized.entrySet().iterator().next().getValue();
        }
        return "";
    }
}
