package TFG.backend.fatigue_drowsiness_detection.adapters.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.ReportUseCase;
import TFG.backend.fatigue_drowsiness_detection.logging.TFG_Logger;
import TFG.backend.fatigue_drowsiness_detection.model.detectionData.ReportPage;

@RestController @RequestMapping("/admin")
public class ReportController {

    private final ReportUseCase reportUseCase;
    private final TFG_Logger logger = new TFG_Logger();

    public ReportController(ReportUseCase reportUseCase) {
        this.reportUseCase = reportUseCase;
    }

    @GetMapping("/reports")
    public Page<ReportPage> getReports(
            @PageableDefault(size = 10, sort = "latestTimestampCamera", direction = Sort.Direction.DESC) Pageable pageable) {

        logger.Info("Getting reports");
        return reportUseCase.getReports(pageable);
    }
}
