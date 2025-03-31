package TFG.backend.fatigue_drowsiness_detection.application.utils;

/**
 * @author mpages1
 */

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import TFG.backend.fatigue_drowsiness_detection.app.DetectionLogger;

@Component
public class HttpServiceClient {

	private static final DetectionLogger logger = new DetectionLogger();
	private final RestTemplate restTemplate;

	public HttpServiceClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void initializeCamera(String url) {
		try {
			logger.logInfo("Initializing Camera Service at " + url);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("shape_predictor_path", "camera_based_detection/shape_predictor_68_face_landmarks.dat");
			requestBody.put("alarm_path", "camera_based_detection/alarm.wav");
			requestBody.put("webcam_index", 0);

			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
			restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			logger.logInfo("Camera Service initialized successfully.");
		} catch (Exception e) {
			logger.logError("Error initializing Camera Service: " + e.getMessage());
		}
	}

	public void initializeService(String url, String serviceName) {
		try {
			logger.logInfo("Initializing " + serviceName + " at " + url);
			restTemplate.exchange(url, HttpMethod.POST, null, String.class);
			logger.logInfo(serviceName + " initialized successfully.");
		} catch (Exception e) {
			logger.logError("Error initializing " + serviceName + ": " + e.getMessage());
		}
	}

	public void initializeCombinedDetection(String url) {
		try {
			logger.logInfo("Initializing Combined Detection Service at " + url);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, String> requestBody = new HashMap<>();

			HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
			restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			logger.logInfo("Combined Detection Service initialized successfully.");
		} catch (ResourceAccessException e) {
			logger.logError("ERROR: Unable to reach Flask server for Combined Detection Service. "
					+ "Connection refused: " + e.getMessage());
		} catch (HttpClientErrorException e) {
			logger.logError("HTTP ERROR: " + e.getStatusCode() + " while initializing Combined Detection Service: "
					+ e.getMessage());
		} catch (Exception e) {
			logger.logError("Unexpected ERROR initializing Combined Detection Service: " + e.getMessage());
		}
	}

	public void startService(String url, int driverId, String serviceName) {
		try {
			logger.logInfo("Starting " + serviceName + " for driver " + driverId + " at " + url);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, Integer> requestBody = new HashMap<>();
			requestBody.put("driver_id", driverId);

			HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(requestBody, headers);
			restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			logger.logInfo(serviceName + " successfully started for driver " + driverId);
		} catch (Exception e) {
			logger.logError("Error starting " + serviceName + " for driver " + driverId + ": " + e.getMessage());
		}
	}
}
