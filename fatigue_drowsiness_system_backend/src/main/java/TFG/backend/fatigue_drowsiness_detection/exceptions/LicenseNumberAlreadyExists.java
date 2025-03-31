package TFG.backend.fatigue_drowsiness_detection.exceptions;

public class LicenseNumberAlreadyExists extends RuntimeException {

    private static final long serialVersionUID = 2826878935855817417L;

    public LicenseNumberAlreadyExists(String licenseNumber) {
        super("License number " + licenseNumber + " already exists");
    }

}
