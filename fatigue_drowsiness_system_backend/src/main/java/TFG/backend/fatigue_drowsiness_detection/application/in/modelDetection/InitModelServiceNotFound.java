package TFG.backend.fatigue_drowsiness_detection.application.in.modelDetection;

public class InitModelServiceNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InitModelServiceNotFound() {
		super("Error: Some service URLs are null! Check application.properties.");
	}

}
