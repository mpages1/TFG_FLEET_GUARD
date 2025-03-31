package TFG.backend.fatigue_drowsiness_detection.application.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
*@author mpages1
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import TFG.backend.fatigue_drowsiness_detection.app.AppConfig;
import TFG.backend.fatigue_drowsiness_detection.app.DetectionLogger;
import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.StopAllDetectionUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.out.DriverRepository;
import TFG.backend.fatigue_drowsiness_detection.application.out.UserRepository;
import TFG.backend.fatigue_drowsiness_detection.model.driver.Driver;
import TFG.backend.fatigue_drowsiness_detection.model.user.UserModel;

@Service
public class StopAllService implements StopAllDetectionUseCase {

	private static final DetectionLogger logger = new DetectionLogger();

	private final RestTemplate restTemplate;
	private final AppConfig appConfig;
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;

	@Autowired
    public StopAllService(AppConfig appConfig, RestTemplate restTemplate, UserRepository userRepository,
            DriverRepository driverRepository) {
		this.appConfig = appConfig;
		this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
	}

	@Override
    public void stopAllDetections(Integer userId) {
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
		logger.logInfo("Stopping all detection services for driver " + driverId + "...");

		stopDetection(appConfig.getSTOP_CAMERA_SERVICE_URL(), driverId, "Camera Service");
		stopDetection(appConfig.getSTOP_TACHOGRAPH_SERVICE_URL(), driverId, "Tachograph Service");
		stopDetection(appConfig.getSTOP_COMBINED_SERVICE_URL(), driverId, "Combined Detection Service");

		logger.logInfo("All detection services stopped for driver " + driverId);
	}

	private void stopDetection(String url, int driverId, String serviceName) {
		try {
			logger.logInfo("Stopping " + serviceName + " for driver " + driverId + " at " + url);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, Integer> requestBody = new HashMap<>();
			requestBody.put("driver_id", driverId);

			HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(requestBody, headers);
			restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			logger.logInfo(serviceName + " successfully stopped for driver " + driverId);
		} catch (Exception e) {
			logger.logError("Error stopping " + serviceName + " for driver " + driverId + ": " + e.getMessage());
		}
	}
}
