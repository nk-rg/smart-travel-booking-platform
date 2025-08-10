package stbp.travelpackageservice.notificationservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import stbp.core.dto.UserResponse;
import stbp.core.dto.BookingEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;
    private final UserServiceClient userServiceClient;

    public void sendBookingCreatedNotification(BookingEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(getUserEmail(event.getUserId())); // TODO: fetch from user service?
            message.setSubject("Booking Created - Smart Travel Booking Platform");
            message.setText("""
                Dear Customer,
                
                Your booking has been created successfully!
                
                Booking Details:
                Package ID: %s
                Quantity: %d
                Total Price: $%.2f
                Booking Time: %s
                
                Thank you for choosing our travel platform!
                
                Best regards,
                Smart Travel Booking Team
                """.formatted(
                    event.getPackageId(),
                    event.getQuantity(),
                    event.getTotalPrice(),
                    event.getTimestamp()
                ));

            mailSender.send(message);
            log.info("Booking created notification sent to user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to send booking created notification to user: {}", event.getUserId(), e);
        }
    }

    public void sendBookingConfirmedNotification(BookingEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(getUserEmail(event.getUserId()));
            message.setSubject("Booking Confirmed - Smart Travel Booking Platform");
            message.setText("""
                Dear Customer,
                
                Great news! Your booking has been confirmed!
                
                Booking Details:
                Package ID: %s
                Quantity: %d
                Total Price: $%.2f
                Confirmed Time: %s
                
                Please prepare for your amazing journey!
                
                Best regards,
                Smart Travel Booking Team
                """.formatted(
                    event.getPackageId(),
                    event.getQuantity(),
                    event.getTotalPrice(),
                    event.getTimestamp()
                ));

            mailSender.send(message);
            log.info("Booking confirmed notification sent to user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to send booking confirmed notification to user: {}", event.getUserId(), e);
        }
    }

    public void sendBookingCancelledNotification(BookingEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(getUserEmail(event.getUserId()));
            message.setSubject("Booking Cancelled - Smart Travel Booking Platform");
            message.setText("""
                Dear Customer,
                
                Your booking has been cancelled as requested.
                
                Cancelled Booking Details:
                Package ID: %s
                Quantity: %d
                Total Price: $%.2f
                Cancelled Time: %s
                
                If you have any questions, please contact our support team.
                We hope to serve you again in the future!
                
                Best regards,
                Smart Travel Booking Team
                """.formatted(
                    event.getPackageId(),
                    event.getQuantity(),
                    event.getTotalPrice(),
                    event.getTimestamp()
                ));

            mailSender.send(message);
            log.info("Booking cancelled notification sent to user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to send booking cancelled notification to user: {}", event.getUserId(), e);
        }
    }

    private String getUserEmail(String userId) {
        try {
            UserResponse user = userServiceClient.getUserById(userId);
            if (user != null && user.getEmail() != null) {
                return user.getEmail();
            } else {
                log.warn("User not found or email is null for userId: {}", userId);
                return userId + "@example.com"; // Fallback
            }
        } catch (Exception e) {
            log.error("Failed to fetch user email for userId: {}", userId, e);
            return userId + "@example.com"; // Fallback
        }
    }

}
