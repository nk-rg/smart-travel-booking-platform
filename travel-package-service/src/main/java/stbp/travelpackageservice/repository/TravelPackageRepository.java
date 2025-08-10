package stbp.travelpackageservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import stbp.travelpackageservice.model.TravelPackage;

import java.math.BigDecimal;

@Repository
public interface TravelPackageRepository extends ReactiveCrudRepository<TravelPackage, String> {
    
    Flux<TravelPackage> findByNameContainingIgnoreCase(String name);
    
    Flux<TravelPackage> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("""
        SELECT * FROM travel_packages 
        ORDER BY created_at DESC
        """)
    Flux<TravelPackage> findAllOrderByCreatedAtDesc();
}
