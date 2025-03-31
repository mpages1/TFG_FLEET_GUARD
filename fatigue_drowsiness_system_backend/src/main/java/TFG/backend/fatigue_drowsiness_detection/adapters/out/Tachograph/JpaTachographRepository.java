package TFG.backend.fatigue_drowsiness_detection.adapters.out.Tachograph;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import TFG.backend.fatigue_drowsiness_detection.application.in.driver.DriverNotFoundException;
import TFG.backend.fatigue_drowsiness_detection.application.out.TachographRepository;
import TFG.backend.fatigue_drowsiness_detection.model.detectionData.Tachograph;

@Repository
public class JpaTachographRepository implements TachographRepository {

    private final SpringDataTachographRepository springDataTachographRepository;

    public JpaTachographRepository(SpringDataTachographRepository springDataTachographRepository) {
        this.springDataTachographRepository = springDataTachographRepository;
    }

    @Override
    public Optional<Float> getDrivingTime(Integer driverId) {
        
        Optional<TachographEntity> tachographEntity = springDataTachographRepository.findTopByDriverIdOrderByTimestampDescIdDesc(driverId);
         
        if (tachographEntity.isPresent()) {
            return Optional.of(tachographEntity.get().getDrivingTime());
        }
        else {
            throw new DriverNotFoundException("Driver driving time not found by driverId" + driverId);
        }
    }

    @Override
    public Optional<Tachograph> getTachograph(Integer driverId) {
        Optional<TachographEntity> tachographEntity = springDataTachographRepository
                .findTopByDriverIdOrderByTimestampDescIdDesc(driverId);

        if (tachographEntity.isPresent()) {
            return Optional.of(TachographMapper.toModel(tachographEntity.get()));
        }
        else {
            throw new DriverNotFoundException("Driver driving time not found by driverId" + driverId);
        }
    }

    @Override
    public Optional<Tachograph> getLatestByDriverId(Integer driverId) {
        Optional<TachographEntity> tachographEntity = springDataTachographRepository
                .findTopByDriverIdOrderByTimestampDescIdDesc(driverId);
        if (tachographEntity.isPresent()) {
            return Optional.of(TachographMapper.toModel(tachographEntity.get()));
        }
        else {
            throw new DriverNotFoundException("Driver driving time not found by driverId" + driverId);
        }
    }

}
