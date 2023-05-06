package ua.illia.estore.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UsernameValidator implements ConstraintValidator<Username, String> {

    public static final String USERNAME_REGEX_PATTERN = "^[a-zA-Z](?:(?:(?:[a-zA-Z0-9\\.](?<!\\.\\.))(?<!\\.\\d))(?<!\\d\\.)){2,30}(?:(?:[a-zA-Z0-9](?<!\\.\\d))(?<!\\d\\.))$";
    public static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX_PATTERN);
    private static final int USERNAME_MIN_LENGTH = 3;
    private static final int USERNAME_MAX_LENGTH = 32;
    private static final String USERNAME_REGEX_CHARACTERS_PATTERN = "[a-zA-Z0-9\\.]";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isNotEmpty(value) &&
                validLength(value, context) &&
                validCharacters(value, context) &&
                validStructure(value, context) &&
                validPattern(value, context);
    }

    private boolean validPattern(String value, ConstraintValidatorContext context) {
        if (USERNAME_PATTERN.matcher(value).matches()) {
            return true;
        }
        setMessage(context, "Invalid username");
        log.error("Unhandled restrictions was found for username: " + value);
        return false;
    }

    private boolean validStructure(String value, ConstraintValidatorContext context) {
        if (StringUtils.isNumeric(value.substring(0, 1)) || value.startsWith(".")) {
            setMessage(context, "Username should start with letter");
            return false;
        }
        if (value.contains("..")) {
            setMessage(context, "Username should not contain two dot separators in a row");
            return false;
        }
        if (value.endsWith(".")) {
            setMessage(context, "Username should not end with a dot");
            return false;
        }
        if (hasNumberNearDot(value)) {
            setMessage(context, "Username should not contain numbers adjacent to dot");
            return false;
        }
        return true;
    }

    private boolean hasNumberNearDot(String value) {
        char[] chars = value.toCharArray();
        for (int i = 1; i < chars.length - 1; i++) {
            if (chars[i] >= '0' && chars[i] <= '9' && (chars[i - 1] == '.' || chars[i + 1] == '.')) {
                return true;
            }
        }
        return false;
    }

    private boolean validCharacters(String value, ConstraintValidatorContext context) {
        String incorrectCharacters = value.replaceAll(USERNAME_REGEX_CHARACTERS_PATTERN, "");
        if (incorrectCharacters.length() > 0) {
            setMessage(context, "Username should not contain following characters: " +
                    incorrectCharacters.chars()
                            .distinct()
                            .mapToObj(c -> String.valueOf((char) c))
                            .collect(Collectors.joining()));
            return false;
        }
        return true;
    }

    private boolean validLength(String value, ConstraintValidatorContext context) {
        if (value.length() < USERNAME_MIN_LENGTH) {
            setMessage(context, "Username must be equal to or greater than " + USERNAME_MIN_LENGTH);
            return false;
        }
        if (value.length() > USERNAME_MAX_LENGTH) {
            setMessage(context, "Username must be equal to or less than " + USERNAME_MAX_LENGTH);
            return false;
        }
        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
