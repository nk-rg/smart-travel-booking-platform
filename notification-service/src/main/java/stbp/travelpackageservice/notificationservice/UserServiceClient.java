package stbp.travelpackageservice.notificationservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import stbp.core.dto.UserResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {
    
    private final RestTemplate restTemplate;
    
    @Value("${user.service.url:http://user-service:8084}")
    private String userServiceUrl;
    
    public UserResponse getUserById(String userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId;
            log.debug("Calling user service at: {}", url);
            
            UserResponse userResponse = restTemplate.getForObject(url, UserResponse.class);
            log.debug("User found: {}", userResponse != null ? userResponse.getEmail() : "null");
            
            return userResponse;
        } catch (Exception e) {
            log.error("Failed to fetch user with ID: {}", userId, e);
            return null;
        }
    }
}