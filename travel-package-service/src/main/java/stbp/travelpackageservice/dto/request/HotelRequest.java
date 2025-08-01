package stbp.travelpackageservice.dto.request;

import lombok.Data;
import stbp.travelpackageservice.model.Hotel;

import java.time.Duration;

@Data
public class HotelRequest {
    private String name;
    private String address;
    private String roomType;
    private Duration checkIn;
    private Duration checkOut;
    private Integer idTravelPackage;

    public Hotel toEntity(String idTravelPackage) {
        Hotel hotel = new Hotel();
        hotel.setIdTravelPackage(idTravelPackage);
        hotel.setName(this.name);
        hotel.setAddress(this.address);
        hotel.setRoomType(this.roomType);
        hotel.setCheckIn(this.checkIn);
        hotel.setCheckOut(this.checkOut);
        return hotel;
    }
}
