package stbp.travelpackageservice.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightAvailabilityRequest {
    private String flightId;
    private int seatsRequested;
}