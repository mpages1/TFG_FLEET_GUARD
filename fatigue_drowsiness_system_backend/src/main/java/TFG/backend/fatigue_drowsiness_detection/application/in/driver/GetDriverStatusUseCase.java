package TFG.backend.fatigue_drowsiness_detection.application.in.driver;

import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverStatus;

public interface GetDriverStatusUseCase {
    DriverStatus getDriverStatus(Integer driverId);
}
