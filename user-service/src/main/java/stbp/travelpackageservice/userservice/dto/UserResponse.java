package stbp.travelpackageservice.userservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import stbp.core.dto.UserResponse as CoreUserResponse;
import stbp.travelpackageservice.userservice.User;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends CoreUserResponse {
    
    public static UserResponse fromUser(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setRole(user.getRole().toString());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}