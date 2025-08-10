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
            message.setText(String.format(
                "Dear Customer,\n\n" +
                "Your booking has been created successfully!\n\n" +
                "Booking Details:\n" +
                "Package ID: %s\n" +
                "Quantity: %d\n" +
                "Total Price: $%.2f\n" +
                "Booking Time: %s\n\n" +
                "Thank you for choosing our travel platform!\n\n" +
                "Best regards,\n" +
                "Smart Travel Booking Team",
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
            message.setText(String.format(
                "Dear Customer,\n\n" +
                "Great news! Your booking has been confirmed!\n\n" +
                "Booking Details:\n" +
                "Package ID: %s\n" +
                "Quantity: %d\n" +
                "Total Price: $%.2f\n" +
                "Confirmed Time: %s\n\n" +
                "Please prepare for your amazing journey!\n\n" +
                "Best regards,\n" +
                "Smart Travel Booking Team",
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
            message.setText(String.format(
                "Dear Customer,\n\n" +
                "Your booking has been cancelled as requested.\n\n" +
                "Cancelled Booking Details:\n" +
                "Package ID: %s\n" +
                "Quantity: %d\n" +
                "Total Price: $%.2f\n" +
                "Cancelled Time: %s\n\n" +
                "If you have any questions, please contact our support team.\n" +
                "We hope to serve you again in the future!\n\n" +
                "Best regards,\n" +
                "Smart Travel Booking Team",
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
