package TFG.backend.fatigue_drowsiness_detection.application.in.driver;

import TFG.backend.fatigue_drowsiness_detection.model.auth.LoginRequest;
import TFG.backend.fatigue_drowsiness_detection.model.auth.LoginResponse;

public interface LoginUserUseCase {
    LoginResponse login(LoginRequest login);
}
