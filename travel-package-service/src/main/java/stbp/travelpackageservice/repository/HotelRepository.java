package stbp.travelpackageservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import stbp.travelpackageservice.model.Hotel;

public interface HotelRepository extends ReactiveCrudRepository<Hotel, String> {
    @Transactional
    @Query("""
    UPDATE hotel h 
    SET h.available_rooms = h.available_rooms - :quantity
    WHERE h.id_travel_package = :packageId 
    AND h.available_rooms >= :quantity
    """)
    Mono<Integer> reserveRooms(@Param("quantity") Integer quantity, @Param("packageId") String packageId);

    @Transactional
    @Query("""
    UPDATE hotel h 
    SET h.available_rooms = h.available_rooms + :quantity
    WHERE h.id_travel_package = :packageId
    """)
    Mono<Integer> releaseRooms(@Param("quantity") Integer quantity, @Param("packageId") String packageId);
}
