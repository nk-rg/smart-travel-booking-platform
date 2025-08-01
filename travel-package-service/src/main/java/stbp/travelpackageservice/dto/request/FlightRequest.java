package stbp.travelpackageservice.dto.request;

import lombok.Data;
import stbp.travelpackageservice.model.Flight;

@Data
public class FlightRequest {
    private String flightNumber;
    private String departureTime;
    private String arrivalTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private String airline;

    public Flight toEntity(String idTravelPackage) {
        Flight flight = new Flight();
        flight.setFlightNumber(this.flightNumber);
        flight.setDepartureTime(this.departureTime);
        flight.setArrivalTime(this.arrivalTime);
        flight.setTotalSeats(this.totalSeats);
        flight.setAvailableSeats(this.availableSeats);
        flight.setAirline(this.airline);
        flight.setIdTravelPackage(idTravelPackage);
        return flight;
    }
}
