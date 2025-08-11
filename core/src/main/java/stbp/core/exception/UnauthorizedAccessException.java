package stbp.core.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedAccessException extends BaseException {
    
    public UnauthorizedAccessException(String action, String resource) {
        super(String.format("User is not authorized to %s %s", action, resource), 
              HttpStatus.FORBIDDEN, 
              "UNAUTHORIZED_ACCESS");
    }
    
    public UnauthorizedAccessException(String message) {
        super(message, HttpStatus.FORBIDDEN, "UNAUTHORIZED_ACCESS");
    }
}