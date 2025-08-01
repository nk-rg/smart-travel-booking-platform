package stbp.travelpackageservice.bookingservice;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookResponse {
    private String userId;
    private String packageId;
    private Integer quantity;
    private BigDecimal packagePrice;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
}
