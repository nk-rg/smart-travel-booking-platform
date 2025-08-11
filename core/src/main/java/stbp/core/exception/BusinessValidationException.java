package stbp.core.exception;

import org.springframework.http.HttpStatus;

public class BusinessValidationException extends BaseException {
    
    public BusinessValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "BUSINESS_VALIDATION_ERROR");
    }
    
    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST, "BUSINESS_VALIDATION_ERROR");
    }
}