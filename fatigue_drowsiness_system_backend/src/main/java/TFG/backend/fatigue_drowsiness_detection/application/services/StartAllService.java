package TFG.backend.fatigue_drowsiness_detection.application.services;

import java.util.Optional;

/**
 * @author mpages1
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import TFG.backend.fatigue_drowsiness_detection.app.DetectionLogger;
import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.CameraDetectionUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.CombinedDetectionUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.StartAllDetectionUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.TachographDetectionUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.out.DriverRepository;
import TFG.backend.fatigue_drowsiness_detection.application.out.UserRepository;
import TFG.backend.fatigue_drowsiness_detection.model.driver.Driver;
import TFG.backend.fatigue_drowsiness_detection.model.user.UserModel;

@Service
public class StartAllService implements StartAllDetectionUseCase {

	private static final DetectionLogger logger = new DetectionLogger();

	private final CameraDetectionUseCase cameraServiceUsecase;
	private final TachographDetectionUseCase tachographServiceUsecase;
	private final CombinedDetectionUseCase combinedServiceUsecase;
	private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    @Value("${detection.init.delay.ms:3000}")
    private int delayMs;

	@Autowired
	public StartAllService(DriverRepository driverRepository, CameraDetectionUseCase cameraServiceUsecase,
            TachographDetectionUseCase tachographServiceUsecase, CombinedDetectionUseCase combinedServiceUsecase,
            UserRepository userRepository) {
		this.driverRepository = driverRepository;
		this.cameraServiceUsecase = cameraServiceUsecase;
		this.tachographServiceUsecase = tachographServiceUsecase;
		this.combinedServiceUsecase = combinedServiceUsecase;
        this.userRepository = userRepository;
	}

	@Override
    public void startAllDetections(Integer userId) {
        Optional<UserModel> user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
		}
        Optional<Driver> driver = driverRepository.findByUserId(userId);
           if (driver == null) {
                throw new RuntimeException("Driver not found");
            }

            logger.logInfo("Starting all detection services for driver " + driver.get().getId());

            Integer driverId = driver.get().getId();
		if (!combinedServiceUsecase.isAdaBoostTrained()) {
			logger.logWarning("AdaBoost model is not trained. Attempting to train before starting detection...");
			combinedServiceUsecase.trainAdaBoostIfNecessary();
		}

		cameraServiceUsecase.initializeCamera();
		tachographServiceUsecase.initializeTachograph();
		combinedServiceUsecase.initializeCombinedDetection();

		cameraServiceUsecase.startCamera(driverId);
		tachographServiceUsecase.startTachograph(driverId);
		combinedServiceUsecase.startCombinedDetection(driverId);

		   try {
               Thread.sleep(delayMs);
		    } catch (InterruptedException e) {
		        Thread.currentThread().interrupt();
		        throw new RuntimeException("Interrupted while waiting for models to initialize", e);
		    }

		   
		logger.logInfo("All detection services successfully started for driver " + driverId);
	}
}
