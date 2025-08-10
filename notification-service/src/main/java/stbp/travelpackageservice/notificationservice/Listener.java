package stbp.travelpackageservice.notificationservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import stbp.core.dto.BookingEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class Listener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "booking-events", groupId = "notification-group")
    public void handleBookingEvent(BookingEvent event) {
        log.info("Received booking event for notification: {}", event);
        
        switch (event.getStatus()) {
            case "CREATED":
                notificationService.sendBookingCreatedNotification(event);
                break;
            case "CONFIRMED":
                notificationService.sendBookingConfirmedNotification(event);
                break;
            case "CANCELLED":
                notificationService.sendBookingCancelledNotification(event);
                break;
            default:
                log.warn("Unknown booking event status: {}", event.getStatus());
        }
    }
}
