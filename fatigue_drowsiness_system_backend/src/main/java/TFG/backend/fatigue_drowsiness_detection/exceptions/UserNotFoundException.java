package TFG.backend.fatigue_drowsiness_detection.exceptions;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 985454319538979282L;

    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }

}
