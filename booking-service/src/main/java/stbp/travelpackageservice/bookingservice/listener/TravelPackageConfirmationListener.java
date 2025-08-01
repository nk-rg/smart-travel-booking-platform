package stbp.travelpackageservice.bookingservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import stbp.travelpackageservice.bookingservice.dto.TravelPackageConfirmationEvent;
import stbp.travelpackageservice.bookingservice.service.AvailabilityValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class TravelPackageConfirmationListener {

    private final AvailabilityValidationService availabilityValidationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "travel-package-confirmation", groupId = "booking-service-group")
    public void handleTravelPackageConfirmation(String message) {
        try {
            log.info("Received travel package confirmation: {}", message);
            
            TravelPackageConfirmationEvent confirmationEvent = objectMapper.readValue(message, TravelPackageConfirmationEvent.class);
            
            if (confirmationEvent.getPackageId() == null || confirmationEvent.getUserId() == null || confirmationEvent.getQuantity() == null) {
                log.error("Missing required fields in travel package confirmation");
                return;
            }

            if (confirmationEvent.getHotelId() != null && confirmationEvent.getAvailableRooms() != null) {
                availabilityValidationService.storeHotelAvailability(confirmationEvent.getHotelId(), confirmationEvent.getAvailableRooms());
                log.info("Stored hotel availability: hotelId={}, availableRooms={}", 
                        confirmationEvent.getHotelId(), confirmationEvent.getAvailableRooms());
            }

            if (confirmationEvent.getFlightId() != null && confirmationEvent.getAvailableSeats() != null) {
                availabilityValidationService.storeFlightAvailability(confirmationEvent.getFlightId(), confirmationEvent.getAvailableSeats());
                log.info("Stored flight availability: flightId={}, availableSeats={}", 
                        confirmationEvent.getFlightId(), confirmationEvent.getAvailableSeats());
            }

            log.info("Travel package availability data stored successfully for packageId: {}", confirmationEvent.getPackageId());
            
        } catch (Exception e) {
            log.error("Error processing travel package confirmation: {}", message, e);
        }
    }
}