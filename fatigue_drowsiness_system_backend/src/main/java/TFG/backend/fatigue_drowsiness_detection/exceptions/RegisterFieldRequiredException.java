package TFG.backend.fatigue_drowsiness_detection.exceptions;

public class RegisterFieldRequiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegisterFieldRequiredException(String msg) {
        super(msg);
    }

}
