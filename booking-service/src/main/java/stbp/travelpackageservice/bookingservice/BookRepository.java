package stbp.travelpackageservice.bookingservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUserId(String userId);
    List<Book> findByPackageId(String packageId);
}
