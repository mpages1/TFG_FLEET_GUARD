package TFG.backend.fatigue_drowsiness_detection.adapters.in;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TFG.backend.fatigue_drowsiness_detection.application.services.CheckDriverAlertnessService;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverMetrics;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverStatus;

@RestController @RequestMapping("/ws-data")
public class DriverWebSocketController {

    private final CheckDriverAlertnessService alertnessService;
    private final SimpMessagingTemplate messagingTemplate;

    public DriverWebSocketController(CheckDriverAlertnessService alertnessService,
            SimpMessagingTemplate messagingTemplate) {
        this.alertnessService = alertnessService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/send/{userId}")
    public ResponseEntity<String> sendDriverUpdates(@PathVariable Integer userId) {
        DriverStatus status = alertnessService.getDriverStatus(userId);
        DriverMetrics metrics = alertnessService.getDriverMetrics(userId);

        messagingTemplate.convertAndSend("/topic/driver/" + userId + "/status", status);
        messagingTemplate.convertAndSend("/topic/driver/" + userId + "/metrics", metrics);

        return ResponseEntity.ok("Updates sent via WebSocket");
    }
}
