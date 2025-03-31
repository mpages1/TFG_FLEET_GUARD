package TFG.backend.fatigue_drowsiness_detection.application.in.driver;

public class DriverNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3574342571187209117L;

    public DriverNotFoundException(Integer driverId) {
		super("Driver with id " + driverId + " not found");
	}

    public DriverNotFoundException(String string) {
        super(string);
    }

}
