package TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection;

import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverAlertnessPrediction;
public interface NotificationUseCase {
    void notifyIfNeeded(DriverAlertnessPrediction prediction);
}
