package stbp.travelpackageservice.bookingservice;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookRequest {
    private String userId;
    private String packageId;
    private Integer quantity;
    private BigDecimal packagePrice;
    private String hotelId;
    private String flightId;
}
