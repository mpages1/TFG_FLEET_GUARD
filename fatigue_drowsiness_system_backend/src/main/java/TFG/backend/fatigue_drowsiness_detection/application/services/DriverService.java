package TFG.backend.fatigue_drowsiness_detection.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.GetAllDriversForAdminUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.UpdateDriverUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.out.DriverRepository;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverForAdmin;
import TFG.backend.fatigue_drowsiness_detection.model.driver.UpdateDriverRequest;

@Service
public class DriverService implements UpdateDriverUseCase, GetAllDriversForAdminUseCase {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public void updateDriver(Integer driverId, UpdateDriverRequest request) {
        driverRepository.updateDriver(driverId, request);
    }

    @Override
    public List<DriverForAdmin> getAllDriversForAdmin() {
        return driverRepository.getAllDriversForAdmin();
    }

}
