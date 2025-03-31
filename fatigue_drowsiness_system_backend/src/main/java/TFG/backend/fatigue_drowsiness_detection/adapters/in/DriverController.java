package TFG.backend.fatigue_drowsiness_detection.adapters.in;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.GetAllDriversForAdminUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.UpdateDriverUseCase;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverForAdmin;
import TFG.backend.fatigue_drowsiness_detection.model.driver.UpdateDriverRequest;

@RestController @RequestMapping("/drivers")
public class DriverController {

    private final UpdateDriverUseCase updateDriverUseCase;
    private final GetAllDriversForAdminUseCase getAllDriversForAdmin;

    public DriverController(UpdateDriverUseCase updateDriverUseCase,
            GetAllDriversForAdminUseCase getAllDriversForAdmin) {
        this.updateDriverUseCase = updateDriverUseCase;
        this.getAllDriversForAdmin = getAllDriversForAdmin;
    }

    @PutMapping("/update")
    public void updateDriver(Integer driverId, UpdateDriverRequest request) {
        updateDriverUseCase.updateDriver(driverId, request);
    }

    @GetMapping("/all")
    public List <DriverForAdmin> getAllDrivers() {
        return getAllDriversForAdmin.getAllDriversForAdmin();
    }

}
