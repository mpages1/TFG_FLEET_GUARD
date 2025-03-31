package TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import TFG.backend.fatigue_drowsiness_detection.model.detectionData.ReportPage;

public interface ReportUseCase {
    Page<ReportPage> getReports(Pageable pageable);

}
