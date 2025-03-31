package TFG.backend.fatigue_drowsiness_detection.adapters.in;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TFG.backend.fatigue_drowsiness_detection.application.in.driver.GetDriverMetricsUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.in.driver.GetDriverStatusUseCase;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverMetrics;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverStatus;

@RestController @RequestMapping("/driver-status")
public class DriverStatusController {

    private final GetDriverStatusUseCase getDriverStatusUseCase;
    private final GetDriverMetricsUseCase getDriverMetricsUseCase;

    public DriverStatusController(GetDriverStatusUseCase getDriverStatusUseCase,
            GetDriverMetricsUseCase getDriverMetricsUseCase) {
        this.getDriverStatusUseCase = getDriverStatusUseCase;
        this.getDriverMetricsUseCase = getDriverMetricsUseCase;
    }

    @GetMapping("/{userId}/status")
    public ResponseEntity<DriverStatus> getDriverStatus(@PathVariable Integer userId) {
        DriverStatus status = getDriverStatusUseCase.getDriverStatus(userId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/{userId}/metrics")
    public ResponseEntity<DriverMetrics> getDriverMetrics(@PathVariable Integer userId) {
        DriverMetrics metrics = getDriverMetricsUseCase.getDriverMetrics(userId);
        return ResponseEntity.ok(metrics);
    }

}

