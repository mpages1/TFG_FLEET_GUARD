package TFG.backend.fatigue_drowsiness_detection.adapters.out.driver;

import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverMetrics;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverStatus;

public interface DriverNotificationPublisher {
    void publishDriverStatus(Integer userId, DriverStatus status);

    void publishDriverMetrics(Integer userId, DriverMetrics metrics);
}
