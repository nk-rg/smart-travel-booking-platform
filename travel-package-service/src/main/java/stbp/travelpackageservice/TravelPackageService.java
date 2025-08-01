package stbp.travelpackageservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stbp.travelpackageservice.dto.listener.BookingEvent;
import stbp.travelpackageservice.dto.request.TravelPackageRequest;
import stbp.travelpackageservice.dto.response.TravelPackageResponse;
import stbp.travelpackageservice.model.TravelPackage;
import stbp.travelpackageservice.repository.TravelPackageRepository;
import stbp.travelpackageservice.repository.HotelRepository;
import stbp.travelpackageservice.repository.FlightRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TravelPackageService {

    private final TravelPackageRepository repository;
    private final HotelRepository hotelRepository;
    private final FlightRepository flightRepository;

    public Flux<TravelPackageResponse> findAll() {
        return repository.findAll()
                .map(TravelPackage::toResponse);
    }

    public Mono<TravelPackageResponse> findById(String id) {
        return repository.findById(id)
                .map(TravelPackage::toResponse);
    }

    public Mono<TravelPackageResponse> save(TravelPackageRequest request) {
        TravelPackage travelPackage = new TravelPackage();
        travelPackage.setName(request.getName());
        travelPackage.setDescription(request.getDescription());
        travelPackage.setPrice(request.getPrice());
        travelPackage.setCreatedAt(LocalDateTime.now());

        return repository.save(travelPackage)
                .map(TravelPackage::toResponse)
                .doOnSuccess(response -> {
                    log.info("Travel package created: {}", response.getName());
                });
    }

    public Mono<TravelPackageResponse> update(String id, TravelPackageRequest request) {
        return repository.findById(id)
                .flatMap(existingPackage -> {
                    existingPackage.setName(request.getName());
                    existingPackage.setDescription(request.getDescription());
                    existingPackage.setPrice(request.getPrice());
                    return repository.save(existingPackage);
                })
                .map(TravelPackage::toResponse);
    }

    public Mono<Boolean> deleteById(String id) {
        return repository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return repository.deleteById(id).thenReturn(true);
                    }
                    return Mono.just(false);
                });
    }

    public Flux<TravelPackageResponse> search(String name, Double minPrice, Double maxPrice) {
        if (name != null && !name.isEmpty()) {
            return repository.findByNameContainingIgnoreCase(name)
                    .map(TravelPackage::toResponse);
        }
        
        if (minPrice != null && maxPrice != null) {
            return repository.findByPriceBetween(BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice))
                    .map(TravelPackage::toResponse);
        }
        
        return findAll();
    }

    public void processBookingEvent(BookingEvent event) {
        log.info("Processing booking event for package: {} with status: {}", event.getPackageId(), event.getStatus());
        
        switch (event.getStatus()) {
            case "CREATED":
                handleBookingCreated(event);
                break;
            case "CANCELLED":
                handleBookingCancelled(event);
                break;
            case "CONFIRMED":
                handleBookingConfirmed(event);
                break;
            default:
                log.warn("Unknown booking status: {}", event.getStatus());
        }
    }

    private void handleBookingCreated(BookingEvent event) {
        log.info("Pre-booking for package: {} with quantity: {}", event.getPackageId(), event.getQuantity());
        
        Mono.zip(
            hotelRepository.reserveRooms(event.getQuantity(), event.getPackageId()),
            flightRepository.reserveSeats(event.getQuantity(), event.getPackageId())
        )
        .doOnSuccess(result -> {
            log.info("Pre-booking successful for package: {} - Hotel rooms reserved: {}, Flight seats reserved: {}", 
                    event.getPackageId(), result.getT1(), result.getT2());
        })
        .doOnError(error -> {
            log.error("Pre-booking failed for package: {}", event.getPackageId(), error);
        })
        .subscribe();
    }

    private void handleBookingCancelled(BookingEvent event) {
        log.info("Cancelling booking for package: {} with quantity: {}", event.getPackageId(), event.getQuantity());
        
        Mono.zip(
            hotelRepository.releaseRooms(event.getQuantity(), event.getPackageId()),
            flightRepository.releaseSeats(event.getQuantity(), event.getPackageId())
        )
        .doOnSuccess(result -> {
            log.info("Booking cancellation successful for package: {} - Hotel rooms released: {}, Flight seats released: {}", 
                    event.getPackageId(), result.getT1(), result.getT2());
        })
        .doOnError(error -> {
            log.error("Booking cancellation failed for package: {}", event.getPackageId(), error);
        })
        .subscribe();
    }

    private void handleBookingConfirmed(BookingEvent event) {
        log.info("Confirming final booking for package: {} with quantity: {}", event.getPackageId(), event.getQuantity());
        
        log.info("Booking confirmed and sealed for package: {} - {} seats/rooms are now permanently booked", 
                event.getPackageId(), event.getQuantity());
    }
}
