package stbp.travelpackageservice.bookingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvailabilityValidationService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String HOTEL_AVAILABILITY_PREFIX = "hotel:availability:";
    private static final String FLIGHT_AVAILABILITY_PREFIX = "flight:availability:";
    private static final Duration CACHE_TTL = Duration.ofHours(1);

    public void storeHotelAvailability(String hotelId, int availableRooms) {
        String cacheKey = HOTEL_AVAILABILITY_PREFIX + hotelId;
        redisTemplate.opsForValue().set(cacheKey, availableRooms, CACHE_TTL);
        log.info("Stored hotel availability: hotelId={}, availableRooms={}", hotelId, availableRooms);
    }

    public void storeFlightAvailability(String flightId, int availableSeats) {
        String cacheKey = FLIGHT_AVAILABILITY_PREFIX + flightId;
        redisTemplate.opsForValue().set(cacheKey, availableSeats, CACHE_TTL);
        log.info("Stored flight availability: flightId={}, availableSeats={}", flightId, availableSeats);
    }

    public boolean validateHotelAvailability(String hotelId, int roomsRequested) {
        String cacheKey = HOTEL_AVAILABILITY_PREFIX + hotelId;
        Integer availableRooms = (Integer) redisTemplate.opsForValue().get(cacheKey);
        
        if (availableRooms == null) {
            log.warn("No hotel availability data found in Redis for hotelId: {}", hotelId);
            return false;
        }

        boolean isAvailable = availableRooms >= roomsRequested;
        log.info("Hotel availability check: hotelId={}, available={}, requested={}, result={}", 
                hotelId, availableRooms, roomsRequested, isAvailable);
        
        return isAvailable;
    }

    public boolean validateFlightAvailability(String flightId, int seatsRequested) {
        String cacheKey = FLIGHT_AVAILABILITY_PREFIX + flightId;
        Integer availableSeats = (Integer) redisTemplate.opsForValue().get(cacheKey);
        
        if (availableSeats == null) {
            log.warn("No flight availability data found in Redis for flightId: {}", flightId);
            return false;
        }

        boolean isAvailable = availableSeats >= seatsRequested;
        log.info("Flight availability check: flightId={}, available={}, requested={}, result={}", 
                flightId, availableSeats, seatsRequested, isAvailable);
        
        return isAvailable;
    }
}