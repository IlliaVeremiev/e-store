package ua.illia.estore.configuration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends ResponseException {

    public ForbiddenException(String message, String exceptionKey) {
        super(message, exceptionKey);
    }

    public ForbiddenException(String message, String exceptionKey, Throwable cause) {
        super(message, exceptionKey, cause);
    }
}
