package TFG.backend.fatigue_drowsiness_detection.application.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import TFG.backend.fatigue_drowsiness_detection.adapters.out.driver.DriverNotificationPublisher;
import TFG.backend.fatigue_drowsiness_detection.application.in.driver.GetDriverMetricsUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.in.driver.GetDriverStatusUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.out.CombinedPredictionsRepository;
import TFG.backend.fatigue_drowsiness_detection.application.out.DriverRepository;
import TFG.backend.fatigue_drowsiness_detection.application.out.TachographRepository;
import TFG.backend.fatigue_drowsiness_detection.application.out.UserRepository;
import TFG.backend.fatigue_drowsiness_detection.exceptions.DriverNotFoundException;
import TFG.backend.fatigue_drowsiness_detection.exceptions.UserNotFoundException;
import TFG.backend.fatigue_drowsiness_detection.logging.TFG_Logger;
import TFG.backend.fatigue_drowsiness_detection.model.detectionData.CombinedPredictions;
import TFG.backend.fatigue_drowsiness_detection.model.detectionData.Tachograph;
import TFG.backend.fatigue_drowsiness_detection.model.driver.Driver;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverMetrics;
import TFG.backend.fatigue_drowsiness_detection.model.driver.DriverStatus;
import TFG.backend.fatigue_drowsiness_detection.model.user.UserModel;

@Service
public class CheckDriverAlertnessService implements GetDriverStatusUseCase, GetDriverMetricsUseCase {

    private final DriverRepository driverRepository;
    private final TachographRepository tachographRepository;
    private final CombinedPredictionsRepository combinedpredictionsRepository;
    private final UserRepository userRepository;
    private final DriverNotificationPublisher notificationPublisher;
    private final TFG_Logger logger = new TFG_Logger();

    public CheckDriverAlertnessService(DriverRepository driverRepository, TachographRepository tachographRepository,
            CombinedPredictionsRepository combinedpredictions, UserRepository userRepository,
            DriverNotificationPublisher notificationPublisher) {
        this.driverRepository = driverRepository;
        this.tachographRepository = tachographRepository;
        this.combinedpredictionsRepository = combinedpredictions;
        this.userRepository = userRepository;
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    public DriverStatus getDriverStatus(Integer userId) {

        Optional<UserModel> user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Optional<Driver> driver = driverRepository.findByUserId(userId);
        if (driver == null) {
            throw new DriverNotFoundException("Driver not found");
        }

        logger.Info("Driver found by userId" + userId);

        Integer driverId = driver.get().getId();

        Optional<Float> drivingTime = tachographRepository.getDrivingTime(driverId);
        if (!drivingTime.isPresent()) {
            throw new DriverNotFoundException("Driver driving time not found by driverId" + driverId);
        }

        Optional<Boolean> fatigueDetected = combinedpredictionsRepository.getFatiguePrediction(driverId);
        if (!fatigueDetected.isPresent()) {
            throw new DriverNotFoundException("Driver fatigue prediction not found by driverId" + driverId);
        }

        DriverStatus driverStatus = new DriverStatus(drivingTime.get(), fatigueDetected.get());
        return driverStatus;
    }

    @Override
    public DriverMetrics getDriverMetrics(Integer userId) {

        Optional<UserModel> user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Optional<Driver> driver = driverRepository.findByUserId(userId);
        if (driver == null) {
            throw new RuntimeException("Driver not found");
        }

        logger.Info("Driver found by userId" + userId);

        Integer driverId = driver.get().getId();

        Optional<Tachograph> tachograph = tachographRepository.getTachograph(driverId);
        if (!tachograph.isPresent()) {
            throw new DriverNotFoundException("Driver tachograph not found by driverId" + driverId);
        }

        Optional<CombinedPredictions> combinedPredictions = combinedpredictionsRepository.getCombinedPredictions(driverId);
        if (!combinedPredictions.isPresent()) {
            throw new DriverNotFoundException("Driver combined predictions not found by driverId" + driverId);
        }

        DriverMetrics driverMetrics = new DriverMetrics(tachograph.get(), combinedPredictions.get());
        return driverMetrics;
    }

    public void evaluateAndNotifyDriver(Integer userId, DriverStatus status, DriverMetrics metrics) {

        notificationPublisher.publishDriverStatus(userId, status);
        notificationPublisher.publishDriverMetrics(userId, metrics);
    }
}
