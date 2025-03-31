package TFG.backend.fatigue_drowsiness_detection.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = -9007632812478083261L;

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
