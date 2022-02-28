package uz.ksinn.utils.exception;

import org.springframework.http.HttpStatus;

public class CoreException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final String code;

    private final String description;

    public CoreException(HttpStatus httpStatus, String code, String description) {
        super(description != null ? String.join(": ", code, description) : code);
        this.httpStatus = httpStatus;
        this.code = code;
        this.description = description;
    }

    public CoreException(String code, String description) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, code, description);
    }

    public CoreException(String code) {
        this(code, null);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}