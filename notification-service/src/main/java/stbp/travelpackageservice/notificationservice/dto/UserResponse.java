package stbp.travelpackageservice.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}