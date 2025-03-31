package TFG.backend.fatigue_drowsiness_detection.model.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class DriverStatus {
    private float drivingTime;
    private boolean adaFatigueDetected;
}
