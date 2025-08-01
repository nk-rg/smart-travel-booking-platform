package stbp.travelpackageservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stbp.travelpackageservice.dto.request.TravelPackageRequest;
import stbp.travelpackageservice.dto.response.TravelPackageResponse;

@RestController
@RequestMapping("/api/travel-packages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TravelPackageController {

    private final TravelPackageService travelPackageService;

    @GetMapping
    public Flux<TravelPackageResponse> getAllPackages() {
        return travelPackageService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TravelPackageResponse>> getPackageById(@PathVariable String id) {
        return travelPackageService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<TravelPackageResponse> createPackage(@RequestBody TravelPackageRequest request) {
        return travelPackageService.save(request);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TravelPackageResponse>> updatePackage(
            @PathVariable String id, 
            @RequestBody TravelPackageRequest request) {
        return travelPackageService.update(id, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePackage(@PathVariable String id) {
        return travelPackageService.deleteById(id)
                .map(deleted -> deleted ? ResponseEntity.ok().<Void>build() : ResponseEntity.notFound().<Void>build());
    }

    @GetMapping("/search")
    public Flux<TravelPackageResponse> searchPackages(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return travelPackageService.search(name, minPrice, maxPrice);
    }
}
