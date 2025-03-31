package TFG.backend.fatigue_drowsiness_detection.exceptions;

public class DriverNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -4852696784441337024L;

    public DriverNotFoundException(String message) {
        super(message);
    }

    public DriverNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
