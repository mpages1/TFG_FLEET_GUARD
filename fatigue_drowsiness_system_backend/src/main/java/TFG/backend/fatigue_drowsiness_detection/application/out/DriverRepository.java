package TFG.backend.fatigue_drowsiness_detection.application.out;

import java.util.List;
import java.util.Optional;

import TFG.backend.fatigue_drowsiness_detection.model.driver.Driver;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverForAdmin;
import TFG.backend.fatigue_drowsiness_detection.model.driver.UpdateDriverRequest;

/**
 * @author mpages1
 */
public interface DriverRepository {

    Optional<Driver> findById(Integer id);
    Driver save(Driver driver);
    boolean existsByLicenseNumber(String licenseNumber);

    Optional<Driver> findByUserId(Integer usererId);

    Driver[] findAll();

    void updateDriver(Integer driverId, UpdateDriverRequest request);

    List<DriverForAdmin> getAllDriversForAdmin();
}
