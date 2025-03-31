/**
 * @author mpages1
 */
package TFG.backend.fatigue_drowsiness_detection.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import TFG.backend.fatigue_drowsiness_detection.app.AppConfig;
import TFG.backend.fatigue_drowsiness_detection.app.DetectionLogger;
import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.CombinedDetectionUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.ReportUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.out.CombinedPredictionsRepository;
import TFG.backend.fatigue_drowsiness_detection.application.utils.HttpServiceClient;
import TFG.backend.fatigue_drowsiness_detection.model.detectionData.ReportPage;

@Service
public class CombinedDetectionService implements CombinedDetectionUseCase, ReportUseCase {

	private final HttpServiceClient httpClient;
	private final AppConfig appConfig;
	private final DetectionLogger logger = new DetectionLogger();
	private final RestTemplate restTemplate = new RestTemplate();
    private final CombinedPredictionsRepository combinedpredictionsRepository;

	@Autowired
    public CombinedDetectionService(AppConfig appConfig, HttpServiceClient httpClient,
            CombinedPredictionsRepository combinedpredictionsRepository) {
		this.appConfig = appConfig;
		this.httpClient = httpClient;
        this.combinedpredictionsRepository = combinedpredictionsRepository;
	}

	@Override
	public void initializeCombinedDetection() {
		httpClient.initializeCombinedDetection(appConfig.getINIT_COMBINED_SERVICE_URL());
	}

	@Override
	public void startCombinedDetection(int driverId) {
		httpClient.startService(appConfig.getSTART_COMBINED_SERVICE_URL(), driverId, "Combined Detection Service");
	}

	@Override
	public boolean isAdaBoostTrained() {
		try {
			String url = appConfig.getCHECK_ADABOOST_MODEL_URL();
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			return response.getStatusCode().is2xxSuccessful();
		} catch (Exception e) {
			logger.logWarning("Unable to check AdaBoost model status: " + e.getMessage());
			return false;
		}
	}

	@Override
	public void trainAdaBoostIfNecessary() {
		try {
			String url = appConfig.getTRAIN_ADABOOST_MODEL_URL();
			ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				logger.logInfo("AdaBoost model trained successfully.");
			} else {
				logger.logError("Failed to train AdaBoost model.");
			}
		} catch (Exception e) {
			logger.logError("ERROR training AdaBoost model: " + e.getMessage());
		}
	}

    @Override
    public Page<ReportPage> getReports(Pageable pageable) {

        Page<ReportPage> reports = combinedpredictionsRepository.findAllReports(pageable);
        return reports;
    }

}
