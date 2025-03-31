package TFG.backend.fatigue_drowsiness_detection.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TFG_Logger {

    private final Logger logger = LoggerFactory.getLogger(TFG_Logger.class);

    public void Info(String message) {
        logger.info("[INFO] " + message);
    }

    public void Warning(String message) {
        logger.warn("[WARNING] " + message);
    }

    public void Error(String message) {
        logger.error("[ERROR] " + message);
    }

    public void Error(String message, Throwable ex) {
        logger.error("[ERROR] " + message, ex);
    }
}
