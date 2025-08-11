package stbp.core.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends BaseException {
    
    public ResourceAlreadyExistsException(String resourceType, String identifier) {
        super(String.format("%s already exists with identifier: %s", resourceType, identifier), 
              HttpStatus.CONFLICT, 
              "RESOURCE_ALREADY_EXISTS");
    }
    
    public ResourceAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, "RESOURCE_ALREADY_EXISTS");
    }
}