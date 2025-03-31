package TFG.backend.fatigue_drowsiness_detection.application.services;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import TFG.backend.fatigue_drowsiness_detection.application.out.CombinedPredictionsRepository;
import TFG.backend.fatigue_drowsiness_detection.application.out.DriverRepository;
import TFG.backend.fatigue_drowsiness_detection.application.out.TachographRepository;
import TFG.backend.fatigue_drowsiness_detection.model.detectionData.CombinedPredictions;
import TFG.backend.fatigue_drowsiness_detection.model.detectionData.Tachograph;
import TFG.backend.fatigue_drowsiness_detection.model.driver.Driver;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverMetrics;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverStatus;

@Component
public class DriverWebSocketScheduler {

    private final CheckDriverAlertnessService alertnessService;
    private final DriverRepository driverRepository;
    private final TachographRepository tachographRepository;
    private final CombinedPredictionsRepository combinedpredictionsRepository;

    private final Map<Integer, Instant> lastTachographSent = new HashMap<>();
    private final Map<Integer, Instant> lastCombinedSent = new HashMap<>();

    private final UserSessionRegistry sessionRegistry;

    public DriverWebSocketScheduler(CheckDriverAlertnessService alertnessService, DriverRepository driverRepository,
            TachographRepository tachographRepository, CombinedPredictionsRepository combinedpredictionsRepository,
            UserSessionRegistry sessionRegistry) {
        this.alertnessService = alertnessService;
        this.driverRepository = driverRepository;
        this.tachographRepository = tachographRepository;
        this.combinedpredictionsRepository = combinedpredictionsRepository;
        this.sessionRegistry = sessionRegistry;
    }

    @Scheduled(fixedDelay = 200)
    public void broadcastDriverData() {
        for (Driver driver : driverRepository.findAll()) {
            
            Integer driverId = driver.getId();

            if (!sessionRegistry.isActive(driver.getUserId()))
                continue;

            Optional<Tachograph> latestTachograph = tachographRepository.getLatestByDriverId(driverId);
            Optional<CombinedPredictions> latestCombined = combinedpredictionsRepository.getLatestByDriverId(driverId);

            if (latestTachograph.isEmpty() || latestCombined.isEmpty()) continue;

            Instant tachographTs = latestTachograph.get().getTimestamp().toInstant();
            Instant combinedTs = latestCombined.get().getLatestTimestampCamera().toInstant();

            boolean isNewTachograph = lastTachographSent.get(driverId) == null || tachographTs.isAfter(lastTachographSent.get(driverId));
            boolean isNewCombined = lastCombinedSent.get(driverId) == null || combinedTs.isAfter(lastCombinedSent.get(driverId));

            if (isNewTachograph || isNewCombined) {
                lastTachographSent.put(driverId, tachographTs);
                lastCombinedSent.put(driverId, combinedTs);

                DriverMetrics metrics = alertnessService.getDriverMetrics(driver.getUserId());
                DriverStatus status = alertnessService.getDriverStatus(driver.getUserId());

                
                alertnessService.evaluateAndNotifyDriver(driver.getUserId(), status, metrics);

        }
    }
}
}
