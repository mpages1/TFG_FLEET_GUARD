package TFG.backend.fatigue_drowsiness_detection.adapters.in;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import TFG.backend.fatigue_drowsiness_detection.app.DetectionLogger;
import TFG.backend.fatigue_drowsiness_detection.application.services.StartAllService;

@RestController @RequestMapping("/detection")
public class StartAllController {

    private final StartAllService startAllService;
    DetectionLogger logger = new DetectionLogger();
    	
    @Autowired
    public StartAllController(StartAllService startAllService) {
        this.startAllService = startAllService;
    }

    @PostMapping("/start_all")
    public String startAllDetections(@RequestParam int userId) {
        startAllService.startAllDetections(userId);
        logger.logInfo("All detection services started for user ID: " + userId);
        return "All detection services started for user ID: " + userId;
    }
}
