package TFG.backend.fatigue_drowsiness_detection.model.detectionData;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class ReportPage {
    private Integer driverId;
    private Date latestTimestampCamera;
    private Float fatigueScore;
    private Boolean detected;
}
