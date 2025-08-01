package stbp.travelpackageservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlightResponse {
    private String flightId;
    private String flightNumber;
    private String departureTime;
    private String arrivalTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private String airline;
}
