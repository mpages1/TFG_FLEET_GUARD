package TFG.backend.fatigue_drowsiness_detection.adapters.out.combined_predictions;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataCombinedpredictionsRepository extends JpaRepository<CombinedPredictionsEntity, Integer> {

    Optional<CombinedPredictionsEntity> findTopByDriverIdOrderByLatestTimestampCameraDescIdDesc(Integer driverId);

    Page<CombinedPredictionsEntity> findAll(Pageable pageable);
}
