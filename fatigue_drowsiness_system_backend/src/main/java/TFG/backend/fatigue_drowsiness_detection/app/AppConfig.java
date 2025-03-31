package TFG.backend.fatigue_drowsiness_detection.app;

/**
 * @author mpages1
 */
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfig {

    private String INIT_CAMERA_SERVICE_URL;
    private String INIT_TACHOGRAPH_SERVICE_URL;
    private String INIT_COMBINED_SERVICE_URL;

    private String START_CAMERA_SERVICE_URL;
    private String START_TACHOGRAPH_SERVICE_URL;
    private String START_COMBINED_SERVICE_URL;

    private String CHECK_ADABOOST_MODEL_URL;
    private String TRAIN_ADABOOST_MODEL_URL;

    private String STOP_CAMERA_SERVICE_URL;
    private String STOP_TACHOGRAPH_SERVICE_URL;
    private String STOP_COMBINED_SERVICE_URL;



}
