package TFG.backend.fatigue_drowsiness_detection.model.driver;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author mpages1
 */
@Accessors(fluent = true) @Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class DriverAlertnessPrediction {

    private Long driverId;
    private boolean adaFatigueDetected;
    private LocalDateTime timestamp;

    public boolean needsAlert() {
        return adaFatigueDetected;
    }
}
