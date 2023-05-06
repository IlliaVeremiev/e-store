package ua.illia.estore.configuration;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;
import ua.illia.estore.configuration.exceptions.BindingValidationException;
import ua.illia.estore.configuration.exceptions.ResponseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ExceptionConfig {

    @Value("${spring.profiles.active:" + Constants.Env.PROD + "}")
    private String activeProfile;

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                if (Constants.Env.PROD.equals(activeProfile) || Constants.Env.DEMO.equals(activeProfile)) {
                    options.excluding(ErrorAttributeOptions.Include.STACK_TRACE);
                }
                Map<String, Object> errors = super.getErrorAttributes(webRequest, options);
                Throwable error = getError(webRequest);
                if (error instanceof ResponseException) {
                    ResponseException exception = (ResponseException) error;
                    List<Map<String, String>> subErrors = new ArrayList<>();
                    errors.put("errors", subErrors);
                    subErrors.add(ImmutableMap.of(
                            "key", exception.getKey(),
                            "message", exception.getMessage()
                    ));
                }
                if (error instanceof BindingValidationException) {
                    BindingValidationException exception = (BindingValidationException) error;
                    List<Map<String, String>> subErrors = new ArrayList<>();
                    errors.put("errors", subErrors);
                    for (ObjectError bindingError : exception.getResult().getAllErrors()) {
                        Map<String, String> subError = new HashMap<>();
                        subError.put("key", ArrayUtils.get(bindingError.getCodes(), 0));
                        subError.put("message", bindingError.getDefaultMessage());
                        subErrors.add(subError);
                    }
                }
                return errors;
            }
        };
    }
}
