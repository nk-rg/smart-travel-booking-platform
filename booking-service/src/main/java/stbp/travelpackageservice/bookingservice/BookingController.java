package stbp.travelpackageservice.bookingservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookResponse> getAllBookings() {
        return bookingService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookingById(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<BookResponse> getBookingsByUserId(@PathVariable String userId) {
        return bookingService.findByUserId(userId);
    }

    @PostMapping
    public BookResponse createBooking(@RequestBody BookRequest request) {
        return bookingService.save(request);
    }


    @PostMapping("/{id}/confirm")
    public ResponseEntity<BookResponse> confirmBooking(@PathVariable Long id) {
        return bookingService.confirmBooking(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookResponse> cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
