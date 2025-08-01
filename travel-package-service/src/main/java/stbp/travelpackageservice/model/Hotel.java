package stbp.travelpackageservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Duration;

@Getter
@Setter
public class Hotel {

    @Id
    private String id;
    private String name;
    private String address;
    private String roomType;
    private Duration checkIn;
    private Duration checkOut;
    private Integer totalRooms;
    private Integer availableRooms;
    private String idTravelPackage;
}
