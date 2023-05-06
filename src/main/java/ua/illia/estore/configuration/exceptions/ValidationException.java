package ua.illia.estore.configuration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends ResponseException {

    public ValidationException(String message, String exceptionKey) {
        super(message, exceptionKey);
    }

    public ValidationException(String message, String exceptionKey, Throwable cause) {
        super(message, exceptionKey, cause);
    }
}
