package TFG.backend.fatigue_drowsiness_detection.application.out;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import TFG.backend.fatigue_drowsiness_detection.model.detectionData.CombinedPredictions;
import TFG.backend.fatigue_drowsiness_detection.model.detectionData.ReportPage;

public interface CombinedPredictionsRepository {

    Optional<Boolean> getFatiguePrediction(Integer driverId);

    Optional<CombinedPredictions> getCombinedPredictions(Integer driverId);

    Optional<CombinedPredictions> getLatestByDriverId(Integer driverId);

    Page<ReportPage> findAllReports(Pageable pageable);
}
