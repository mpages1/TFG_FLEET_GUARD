package TFG.backend.fatigue_drowsiness_detection.adapters.in;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection.StopAllDetectionUseCase;

@RestController
@RequestMapping("/detection")
public class StopAllController {

	private final StopAllDetectionUseCase stopAllDetectionUseCase;

	@Autowired
	public StopAllController(StopAllDetectionUseCase stopAllDetectionUseCase) {
		this.stopAllDetectionUseCase = stopAllDetectionUseCase;
	}

	@PostMapping("/stop_all")
    public ResponseEntity<String> stopAllDetections(@RequestParam Integer userId) {
        stopAllDetectionUseCase.stopAllDetections(userId);
        return ResponseEntity.ok("All detection services stopped for user " + userId);
	}
}
