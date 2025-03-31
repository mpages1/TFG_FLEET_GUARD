package TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection;

import java.util.List;

import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverForAdmin;

public interface GetAllDriversForAdminUseCase {

    List<DriverForAdmin> getAllDriversForAdmin();
}
