/**
 * @author mpages1
 */
package TFG.backend.fatigue_drowsiness_detection.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TFG.backend.fatigue_drowsiness_detection.app.AppConfig;
import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.CameraDetectionUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.utils.HttpServiceClient;

@Service
public class CameraDetectionService implements CameraDetectionUseCase {

	private final HttpServiceClient httpClient;
	private final AppConfig appConfig;

	@Autowired
	public CameraDetectionService(AppConfig appConfig, HttpServiceClient httpClient) {
		this.appConfig = appConfig;
		this.httpClient = httpClient;
	}

	@Override
	public void initializeCamera() {
		httpClient.initializeCamera(appConfig.getINIT_CAMERA_SERVICE_URL());
	}

	@Override
	public void startCamera(int driverId) {
		httpClient.startService(appConfig.getSTART_CAMERA_SERVICE_URL(), driverId, "Camera Service");
	}
}
