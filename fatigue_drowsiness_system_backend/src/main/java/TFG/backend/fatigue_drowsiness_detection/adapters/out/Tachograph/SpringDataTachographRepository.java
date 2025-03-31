package TFG.backend.fatigue_drowsiness_detection.adapters.out.Tachograph;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataTachographRepository extends JpaRepository<TachographEntity, Integer> {

    // Optional<TachographEntity> findTopByDriverIdOrderByTimestampDesc(Integer
    // driverId);

    Optional<TachographEntity> findTopByDriverIdOrderByTimestampDescIdDesc(Integer driverId);

}
