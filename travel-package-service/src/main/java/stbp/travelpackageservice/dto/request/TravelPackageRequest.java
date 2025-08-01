package stbp.travelpackageservice.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TravelPackageRequest {
    private String name;
    private String description;
    private BigDecimal price;
}
