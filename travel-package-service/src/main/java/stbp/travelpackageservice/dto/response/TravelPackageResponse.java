package stbp.travelpackageservice.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TravelPackageResponse {
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime createdAt;
}
