package ua.illia.estore.configuration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError extends ResponseException {

    public InternalServerError(String message, String exceptionKey) {
        super(message, exceptionKey);
    }

    public InternalServerError(String message, String exceptionKey, Throwable cause) {
        super(message, exceptionKey, cause);
    }
}