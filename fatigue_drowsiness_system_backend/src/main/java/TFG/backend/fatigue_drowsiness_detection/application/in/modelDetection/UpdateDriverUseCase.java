package TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection;

import TFG.backend.fatigue_drowsiness_detection.model.driver.UpdateDriverRequest;

public interface UpdateDriverUseCase {

    void updateDriver(Integer driverId, UpdateDriverRequest request);
}
