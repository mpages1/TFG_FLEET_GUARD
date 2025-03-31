package TFG.backend.fatigue_drowsiness_detection.adapters.out.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import TFG.backend.fatigue_drowsiness_detection.app.DetectionLogger;
import TFG.backend.fatigue_drowsiness_detection.application.out.DriverRepository;
import TFG.backend.fatigue_drowsiness_detection.exceptions.DriverNotFoundException;
import TFG.backend.fatigue_drowsiness_detection.model.driver.Driver;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverForAdmin;
import TFG.backend.fatigue_drowsiness_detection.model.driver.UpdateDriverRequest;

/**
 * @author mpages1
 */
@Repository
public class JpaDriverRepository implements DriverRepository {

	private final SpringDataDriverRepository repository;
	DetectionLogger logger = new DetectionLogger();

	public JpaDriverRepository(SpringDataDriverRepository repository) {
		this.repository = repository;
	}


    @Override
    public Optional<Driver> findById(Integer id) {
        Optional<DriverEntity> driverEntity = repository.findById(id);
        if (driverEntity.isPresent()) {
            return Optional.of(DriverMapper.toModel(driverEntity.get()));
        }
        return null;
    }

    @Override
    public Driver save(Driver driver) {
        if (driver == null) {
            throw new DriverNotFoundException("Driver cannot be null");
        }
        DriverEntity driverEntity = DriverMapper.toEntity(driver);
        repository.save(driverEntity);

        return driver;
    }

    @Override
    public boolean existsByLicenseNumber(String licenseNumber) {
        return repository.existsByLicenseNumber(licenseNumber);
    }

    @Override
    public Optional<Driver> findByUserId(Integer usererId) {
        Optional<DriverEntity> driverEntity = repository.findByUserId(usererId);
        if (driverEntity.isPresent()) {
            return Optional.of(DriverMapper.toModel(driverEntity.get()));
        }
        return null;
    }

    public Driver[] findAll() {
        List<DriverEntity> entities = repository.findAll();
        List<Driver> drivers = new ArrayList<>();
        for (DriverEntity e : entities) {
            drivers.add(DriverMapper.toModel(e));
        }
        return drivers.toArray(new Driver[0]);
    }

    @Override
    public void updateDriver(Integer driverId, UpdateDriverRequest request) {
        Optional<DriverEntity> driverEntity = repository.findById(driverId);
        if (!driverEntity.isPresent()) {
            throw new DriverNotFoundException("Driver not found by driverId" + driverId);
        }
        driverEntity.get().setName(request.getName());
        driverEntity.get().setAddress(request.getAddress());
        driverEntity.get().setPhone(request.getPhone());
        driverEntity.get().setLicenseNumber(request.getLicenseNumber());
        driverEntity.get().setDateOfBirth(request.getDateOfBirth());
        repository.save(driverEntity.get());
    }

    @Override
    public List<DriverForAdmin> getAllDriversForAdmin() {
        List<DriverEntity> driverEntities = repository.findAll();

        List<DriverForAdmin> drivers = new ArrayList<>();
        for (DriverEntity e : driverEntities) {
            DriverForAdmin driver = new DriverForAdmin();
            driver.setId(e.getId());
            driver.setName(e.getName());
            driver.setPhone(e.getPhone());
            driver.setLicenseNumber(e.getLicenseNumber());
            drivers.add(driver);
        }
        return drivers;
    }

}
