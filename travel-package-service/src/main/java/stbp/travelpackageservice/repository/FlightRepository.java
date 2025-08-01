package stbp.travelpackageservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import stbp.travelpackageservice.model.Flight;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface FlightRepository extends ReactiveCrudRepository<Flight, String> {

    @Transactional
    @Query("""
    UPDATE flight f 
    SET f.available_seats = f.available_seats - :quantity
    WHERE f.id_travel_package = :packageId 
    AND f.available_seats >= :quantity
    """)
    Mono<Integer> reserveSeats(@Param("quantity") Integer quantity, @Param("packageId") String packageId);

    @Transactional
    @Query("""
    UPDATE flight f 
    SET f.available_seats = f.available_seats + :quantity
    WHERE f.id_travel_package = :packageId
    """)
    Mono<Integer> releaseSeats(@Param("quantity") Integer quantity, @Param("packageId") String packageId);
}