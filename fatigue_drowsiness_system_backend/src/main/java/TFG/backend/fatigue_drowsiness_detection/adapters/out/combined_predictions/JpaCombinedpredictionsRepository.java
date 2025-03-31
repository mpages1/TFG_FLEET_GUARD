package TFG.backend.fatigue_drowsiness_detection.adapters.out.combined_predictions;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import TFG.backend.fatigue_drowsiness_detection.application.out.CombinedPredictionsRepository;
import TFG.backend.fatigue_drowsiness_detection.exceptions.CombinedPredictionsNotFoundException;
import TFG.backend.fatigue_drowsiness_detection.logging.TFG_Logger;
import TFG.backend.fatigue_drowsiness_detection.model.detectionData.CombinedPredictions;
import TFG.backend.fatigue_drowsiness_detection.model.detectionData.ReportPage;

@Repository
public class JpaCombinedpredictionsRepository implements CombinedPredictionsRepository {

    private final SpringDataCombinedpredictionsRepository springDataCombinedpredictionsRepository;
    private final TFG_Logger logger = new TFG_Logger();

    public JpaCombinedpredictionsRepository(
            SpringDataCombinedpredictionsRepository springDataCombinedpredictionsRepository) {
        this.springDataCombinedpredictionsRepository = springDataCombinedpredictionsRepository;
    }

    @Override
    public Optional<Boolean> getFatiguePrediction(Integer driverId) {
        Optional<CombinedPredictionsEntity> entity = springDataCombinedpredictionsRepository
                .findTopByDriverIdOrderByLatestTimestampCameraDescIdDesc(driverId);
        if (!entity.isPresent()) {
            throw new CombinedPredictionsNotFoundException("Driver fatigue prediction not found by driverId" + driverId);
        }
        return Optional.of(entity.get().getAdaFatigueDetected());
    }

    @Override
    public Optional<CombinedPredictions> getCombinedPredictions(Integer driverId) {
        Optional<CombinedPredictionsEntity> entity = springDataCombinedpredictionsRepository.findTopByDriverIdOrderByLatestTimestampCameraDescIdDesc(driverId);
        return Optional.ofNullable(CombinedPredictionsMapper.toModel(entity.get()));
    }

    @Override
    public Optional<CombinedPredictions> getLatestByDriverId(Integer driverId) {
        Optional<CombinedPredictionsEntity> entity = springDataCombinedpredictionsRepository
                .findTopByDriverIdOrderByLatestTimestampCameraDescIdDesc(driverId);
        return Optional.ofNullable(CombinedPredictionsMapper.toModel(entity.get()));
    }

    @Override
    public Page<ReportPage> findAllReports(Pageable pageable) {
//        Page<CombinedPredictionsEntity> page = springDataCombinedpredictionsRepository.findAll(pageable);
//        
//        return page.map(entity -> ReportPage.builder()
//                .driverId(entity.getDriverId())
//                .timestamp(entity.getLatestTimestampCamera())
//                .fatigueScore(entity.getFatigueScore())
//                .detected(entity.getAdaFatigueDetected()).build());

        Page<ReportPage> report = springDataCombinedpredictionsRepository.findAll(pageable)
                .map(entity -> new ReportPage(
                        entity.getDriverId(),
                    entity.getLatestTimestampCamera(),
                    entity.getFatigueScore(),
                    entity.getAdaFatigueDetected()
                ));

        logger.Info("Reports found");
        logger.Info("Reports found by pageable" + pageable);
        return report;


    }
}
