package TFG.backend.fatigue_drowsiness_detection.adapters.out.combined_predictions;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "combined_predictions") @Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class CombinedPredictionsEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "driver_id", nullable = false)
    private Integer driverId;

    @Column(name = "latest_timestamp_camera", nullable = true)
    private Date latestTimestampCamera;

    @Column(name = "eye_aspect_ratio", nullable = true)
    private Float eyeAspectRatio;

    @Column(name = "mouth_aspect_ratio", nullable = true)
    private Float mouthAspectRatio;

    @Column(name = "latest_timestamp_tachograph", nullable = true)
    private Date latestTimestampTachograph;

    @Column(name = "combined_prediction", nullable = true)
    private Float combinedPrediction;

    @Column(name = "fatigue_score", nullable = true)
    private Float fatigueScore;

    @Column(name = "drowsiness_score", nullable = true)
    private Float drowsinessScore;

    @Column(name = "fatigue_detected", nullable = true)
    private Boolean fatigueDetected;

    @Column(name = "drowsiness_detected", nullable = true)
    private Boolean drowsinessDetected;

    @Column(name = "predictions", nullable = true)
    private Integer predictions;

    @Column(name = "ada_fatigue_detected", nullable = true)
    private Boolean adaFatigueDetected;

}
