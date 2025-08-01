package stbp.travelpackageservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Flight {

    @Id
    private String id;
    private String flightNumber;
    private String departureTime;
    private String arrivalTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private String airline;
    private String idTravelPackage;
}
