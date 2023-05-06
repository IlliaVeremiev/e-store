package ua.illia.estore.configuration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BindingValidationException extends RuntimeException {

    private final BindingResult result;

    public BindingValidationException(BindingResult result) {
        this.result = result;
    }

    public BindingResult getResult() {
        return result;
    }
}
