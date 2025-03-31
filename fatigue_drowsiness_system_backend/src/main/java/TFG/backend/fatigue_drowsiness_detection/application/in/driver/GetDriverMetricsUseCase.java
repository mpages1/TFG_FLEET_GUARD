package TFG.backend.fatigue_drowsiness_detection.application.in.driver;

import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverMetrics;

public interface GetDriverMetricsUseCase {
    DriverMetrics getDriverMetrics(Integer driverId);
}
