package stbp.travelpackageservice.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelPackageConfirmationEvent {
    private String packageId;
    private String userId;
    private Integer quantity;
    private String hotelId;
    private Integer availableRooms;
    private String flightId;
    private Integer availableSeats;
}