package stbp.travelpackageservice.bookingservice;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String userId;
    private String packageId;
    private Integer quantity;
    private BigDecimal packagePrice;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private String hotelId;
    private String flightId;

    public BookResponse toResponse() {
        BookResponse response = new BookResponse();
        response.setUserId(this.userId);
        response.setPackageId(this.packageId);
        response.setQuantity(this.quantity);
        response.setPackagePrice(this.packagePrice);
        response.setTotalPrice(this.totalPrice);
        response.setCreatedAt(this.createdAt);
        return response;
    }
}
