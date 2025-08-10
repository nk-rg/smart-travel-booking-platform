package stbp.travelpackageservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import stbp.core.dto.BookingEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class TravelPackageListener {

    private final TravelPackageService travelPackageService;

    @KafkaListener(topics = "booking-events", groupId = "travel-package-group")
    public void handleBookingEvent(BookingEvent event) {
        log.info("Received booking event: {}", event);
        travelPackageService.processBookingEvent(event);
    }
}
