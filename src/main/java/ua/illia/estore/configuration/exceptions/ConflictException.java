package ua.illia.estore.configuration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends ResponseException {

    public ConflictException(String message, String exceptionKey) {
        super(message, exceptionKey);
    }

    public ConflictException(String message, String exceptionKey, Throwable cause) {
        super(message, exceptionKey, cause);
    }
}
