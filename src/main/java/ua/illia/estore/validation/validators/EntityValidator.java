package ua.illia.estore.validation.validators;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import ua.illia.estore.configuration.exceptions.ValidationException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public abstract class EntityValidator<T> {

    public static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+(?:\\d\\x20?){6,14}\\d$");

    public abstract void validate(T entity);

    protected void validationError(String message, String key) {
        throw new ValidationException(message, key);
    }

    protected void notNull(Object value, String fieldName) {
        if (value == null) {
            validationError(fieldName + " should not be null", fieldName);
        }
    }

    protected void notEmpty(String value, String fieldName) {
        notNull(value, fieldName);
        if (StringUtils.isEmpty(value)) {
            validationError(fieldName + " should not be empty", fieldName);
        }
    }

    protected void maxLength(String value, int size, String fieldName) {
        if (value != null && value.length() > size) {
            validationError(fieldName + " should be less or equal to " + size, fieldName);
        }
    }

    protected void fitBigDecimal(BigDecimal value, int p, int s, String fieldName) {
        BigDecimal striped = value.stripTrailingZeros();
        if ((striped.precision() - striped.scale() > p)
                || Math.max(0, striped.scale()) > s) {
            validationError("Unable to save BigDecimal value: " + striped + ", limitation is (" + p + ", " + s + ")", fieldName);
        }
    }

    protected void isEmail(String email, String errorKey) {
        if (!EmailValidator.getInstance().isValid(email)) {
            validationError("Invalid email address", errorKey);
        }
    }

    protected void isPhoneNumber(String phoneNumber, String errorKey) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            validationError("Invalid phone number", errorKey);
        }
    }

    protected void isTrue(boolean value, String message, String errorKey) {
        if (!value) {
            validationError(message, errorKey);
        }
    }
}
