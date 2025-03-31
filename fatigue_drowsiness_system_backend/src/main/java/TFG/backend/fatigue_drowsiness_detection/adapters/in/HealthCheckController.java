package TFG.backend.fatigue_drowsiness_detection.adapters.in;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TFG.backend.fatigue_drowsiness_detection.app.DetectionLogger;

/**
 * @author mpages1
 */


/**
 * Simple health check endpoint to test if the backend is running
 */


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

	DetectionLogger logger = new DetectionLogger();
	
    @GetMapping("/check")
    public Map<String, Object> checkHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        
        logger.log("Health check performed");
        return response;
    }
}
