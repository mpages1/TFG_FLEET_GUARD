package TFG.backend.fatigue_drowsiness_detection.exceptions;

public class CombinedPredictionsNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8790805978945915800L;

    public CombinedPredictionsNotFoundException(String msg) {
        super(msg);
    }

}
