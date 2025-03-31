/**
 * @author mpages1
 */
package TFG.backend.fatigue_drowsiness_detection.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TFG.backend.fatigue_drowsiness_detection.app.AppConfig;
import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.TachographDetectionUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.utils.HttpServiceClient;

@Service
public class TachographDetectionService implements TachographDetectionUseCase {

	private final HttpServiceClient httpClient;
	private final AppConfig appConfig;

	@Autowired
	public TachographDetectionService(AppConfig appConfig, HttpServiceClient httpClient) {
		this.appConfig = appConfig;
		this.httpClient = httpClient;
	}

	@Override
	public void initializeTachograph() {
		httpClient.initializeService(appConfig.getINIT_TACHOGRAPH_SERVICE_URL(), "Tachograph Service");
	}

	@Override
	public void startTachograph(int driverId) {
		httpClient.startService(appConfig.getSTART_TACHOGRAPH_SERVICE_URL(), driverId, "Tachograph Service");
	}
}
