package stbp.core.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends BaseException {
    
    public ExternalServiceException(String serviceName, String operation) {
        super(String.format("Failed to %s from %s service", operation, serviceName), 
              HttpStatus.SERVICE_UNAVAILABLE, 
              "EXTERNAL_SERVICE_ERROR");
    }
    
    public ExternalServiceException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, "EXTERNAL_SERVICE_ERROR");
    }
    
    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause, HttpStatus.SERVICE_UNAVAILABLE, "EXTERNAL_SERVICE_ERROR");
    }
}