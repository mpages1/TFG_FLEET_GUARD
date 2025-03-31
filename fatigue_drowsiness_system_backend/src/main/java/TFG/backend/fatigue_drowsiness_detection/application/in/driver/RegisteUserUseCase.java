package TFG.backend.fatigue_drowsiness_detection.application.in.driver;

import TFG.backend.fatigue_drowsiness_detection.model.auth.RegisterRequest;
import TFG.backend.fatigue_drowsiness_detection.model.auth.RegisterResponse;

public interface RegisteUserUseCase {
    RegisterResponse register(RegisterRequest driver);
}
