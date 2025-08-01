package stbp.travelpackageservice.notificationservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public String sendCustomNotification(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String message) {
        
        notificationService.sendCustomNotification(to, subject, message);
        return "Notification sent successfully!";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Notification service is running!";
    }
}
