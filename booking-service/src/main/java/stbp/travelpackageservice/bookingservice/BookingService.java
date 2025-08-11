package stbp.travelpackageservice.bookingservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import stbp.core.dto.BookingEvent;
import stbp.core.exception.BusinessValidationException;
import stbp.core.exception.ResourceNotFoundException;
import stbp.travelpackageservice.bookingservice.service.AvailabilityValidationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookRepository bookRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AvailabilityValidationService availabilityValidationService;

    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream()
                .map(Book::toResponse)
                .collect(Collectors.toList());
    }

    public BookResponse findById(Long id) {
        return bookRepository.findById(id)
                .map(Book::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    public List<BookResponse> findByUserId(String userId) {
        return bookRepository.findByUserId(userId).stream()
                .map(Book::toResponse)
                .collect(Collectors.toList());
    }

    public BookResponse save(BookRequest request) {
        // Validate availability before creating booking
        if (request.getHotelId() != null) {
            boolean hotelAvailable = availabilityValidationService.validateHotelAvailability(request.getHotelId(), request.getQuantity());
            if (!hotelAvailable) {
                log.warn("Hotel booking rejected - insufficient rooms. HotelId: {}, Requested: {}", request.getHotelId(), request.getQuantity());
                throw new BusinessValidationException("Hotel rooms not available for booking");
            }
        }
        
        if (request.getFlightId() != null) {
            boolean flightAvailable = availabilityValidationService.validateFlightAvailability(request.getFlightId(), request.getQuantity());
            if (!flightAvailable) {
                log.warn("Flight booking rejected - insufficient seats. FlightId: {}, Requested: {}", request.getFlightId(), request.getQuantity());
                throw new BusinessValidationException("Flight seats not available for booking");
            }
        }

        Book book = Book.builder()
                .userId(request.getUserId())
                .packageId(request.getPackageId())
                .quantity(request.getQuantity())
                .packagePrice(request.getPackagePrice())
                .totalPrice(request.getPackagePrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                .createdAt(LocalDateTime.now())
                .hotelId(request.getHotelId())
                .flightId(request.getFlightId())
                .build();

        Book savedBook = bookRepository.save(book);
        
        // Send booking event to Kafka
        BookingEvent event = new BookingEvent();
        event.setUserId(savedBook.getUserId());
        event.setPackageId(savedBook.getPackageId());
        event.setQuantity(savedBook.getQuantity());
        event.setTotalPrice(savedBook.getTotalPrice());
        event.setTimestamp(savedBook.getCreatedAt());
        event.setStatus("CREATED");

        kafkaTemplate.send("booking-events", event);
        log.info("Booking created and event sent: {}", savedBook.getId());

        return savedBook.toResponse();
    }


    public BookResponse confirmBooking(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        
        // TODO: implement confirmation logic/validations?
        
        BookingEvent event = new BookingEvent();
        event.setUserId(book.getUserId());
        event.setPackageId(book.getPackageId());
        event.setQuantity(book.getQuantity());
        event.setTotalPrice(book.getTotalPrice());
        event.setTimestamp(LocalDateTime.now());
        event.setStatus("CONFIRMED");

        kafkaTemplate.send("booking-events", event);
        log.info("Booking confirmed: {}", book.getId());

        return book.toResponse();
    }

    public BookResponse cancelBooking(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        
        BookingEvent event = new BookingEvent();
        event.setUserId(book.getUserId());
        event.setPackageId(book.getPackageId());
        event.setQuantity(book.getQuantity());
        event.setTotalPrice(book.getTotalPrice());
        event.setTimestamp(LocalDateTime.now());
        event.setStatus("CANCELLED");

        kafkaTemplate.send("booking-events", event);
        log.info("Booking cancelled: {}", book.getId());

        return book.toResponse();
    }
}
