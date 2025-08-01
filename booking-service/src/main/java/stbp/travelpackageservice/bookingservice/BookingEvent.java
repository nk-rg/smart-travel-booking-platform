package stbp.travelpackageservice.bookingservice;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingEvent {
    private String userId;
    private String packageId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime timestamp;
    private String message;
}
