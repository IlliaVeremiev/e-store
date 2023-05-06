package ua.illia.estore.configuration.exceptions;

public abstract class ResponseException extends RuntimeException {

    private final String key;

    protected ResponseException(String message, String exceptionKey) {
        super(message);
        this.key = exceptionKey;
    }

    protected ResponseException(String message, String exceptionKey, Throwable cause) {
        super(message, cause);
        this.key = exceptionKey;
    }

    public String getKey() {
        return key;
    }
}
