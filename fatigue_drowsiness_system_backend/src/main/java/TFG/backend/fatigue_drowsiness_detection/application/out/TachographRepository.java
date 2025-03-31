package TFG.backend.fatigue_drowsiness_detection.application.out;

import java.util.Optional;

import TFG.backend.fatigue_drowsiness_detection.model.detectionData.Tachograph;

public interface TachographRepository {
    Optional<Float> getDrivingTime(Integer driverId);
    
    Optional<Tachograph> getTachograph(Integer driverId);

    Optional<Tachograph> getLatestByDriverId(Integer driverId);
}
