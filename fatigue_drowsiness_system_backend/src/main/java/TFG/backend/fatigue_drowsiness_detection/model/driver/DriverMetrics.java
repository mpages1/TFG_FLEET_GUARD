package TFG.backend.fatigue_drowsiness_detection.model.driver;

import TFG.backend.fatigue_drowsiness_detection.model.detectionData.CombinedPredictions;
import TFG.backend.fatigue_drowsiness_detection.model.detectionData.Tachograph;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class DriverMetrics {
    private float drivingTime;
    private float fatigueScore;
    private float drowsinessScore;
    private float fuelConsumption;
    private float drivingDistance;
    private boolean fatigueDetected;

    public DriverMetrics(Tachograph tachograph, CombinedPredictions combinedpredictions) {
        this.drivingTime = tachograph.getDrivingTime();
        this.fatigueScore = combinedpredictions.getFatigueScore();
        this.drowsinessScore = combinedpredictions.getDrowsinessScore();
        this.fuelConsumption = tachograph.getFuelConsumption();
        this.drivingDistance = tachograph.getDrivingDistance();
        this.fatigueDetected = combinedpredictions.getAdaFatigueDetected();
    }

}

