package stbp.travelpackageservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

@Data
@AllArgsConstructor
public class HotelResponse {
    private Integer hotelId;
    private String hotelName;
    private String hotelAddress;
    private String roomType;
    private Duration checkIn;
    private Duration checkOut;
    private Integer totalRooms;
    private Integer availableRooms;
}
