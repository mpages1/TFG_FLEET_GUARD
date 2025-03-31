package TFG.backend.fatigue_drowsiness_detection.model.detectionData;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class CombinedPredictions {
    private Integer id;
    private Integer driverId;
    private Date latestTimestampCamera;
    private Float eyeAspectRatio;
    private Float mouthAspectRatio;
    private Date latestTimestampTachograph;
    private Float combinedPrediction;
    private Float fatigueScore;
    private Float drowsinessScore;
    private Boolean fatigueDetected;
    private Boolean drowsinessDetected;
    private Integer adaPredictions;
    private Boolean adaFatigueDetected;

}
