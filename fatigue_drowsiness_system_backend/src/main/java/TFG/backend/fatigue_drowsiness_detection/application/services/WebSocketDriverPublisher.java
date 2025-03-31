package TFG.backend.fatigue_drowsiness_detection.application.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import TFG.backend.fatigue_drowsiness_detection.adapters.out.driver.DriverNotificationPublisher;
import TFG.backend.fatigue_drowsiness_detection.logging.TFG_Logger;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverMetrics;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverStatus;

@Component
public class WebSocketDriverPublisher implements DriverNotificationPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final TFG_Logger logger = new TFG_Logger();

    public WebSocketDriverPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void publishDriverStatus(Integer userId, DriverStatus status) {
        logger.Info("Publishing driver status: " + status);
        messagingTemplate.convertAndSend("/topic/driver/" + userId + "/status", status);
    }

    @Override
    public void publishDriverMetrics(Integer userId, DriverMetrics metrics) {
        logger.Info("Publishing driver metrics: " + metrics);
        messagingTemplate.convertAndSend("/topic/driver/" + userId + "/metrics", metrics);
    }
}
