package TFG.backend.fatigue_drowsiness_detection.exceptions;

public class RoleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 985454319538979282L;

    public RoleNotFoundException(String msg) {
        super(msg);
    }
}
