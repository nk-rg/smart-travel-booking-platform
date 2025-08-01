package stbp.travelpackageservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import stbp.travelpackageservice.dto.response.TravelPackageResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table("travel_packages")
public class TravelPackage {

    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime createdAt;

    public TravelPackageResponse toResponse() {
        TravelPackageResponse travelPackageResponse = new TravelPackageResponse();
        travelPackageResponse.setName(this.name);
        travelPackageResponse.setDescription(this.description);
        travelPackageResponse.setPrice(this.price);
        travelPackageResponse.setCreatedAt(this.createdAt);
        return travelPackageResponse;
    }
}
