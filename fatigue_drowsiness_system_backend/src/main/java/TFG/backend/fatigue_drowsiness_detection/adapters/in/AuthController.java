package TFG.backend.fatigue_drowsiness_detection.adapters.in;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TFG.backend.fatigue_drowsiness_detection.application.in.driver.LoginUserUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.in.driver.RegisteUserUseCase;
import TFG.backend.fatigue_drowsiness_detection.logging.TFG_Logger;
import TFG.backend.fatigue_drowsiness_detection.model.auth.LoginRequest;
import TFG.backend.fatigue_drowsiness_detection.model.auth.LoginResponse;
import TFG.backend.fatigue_drowsiness_detection.model.auth.RegisterRequest;
import TFG.backend.fatigue_drowsiness_detection.model.auth.RegisterResponse;

@RestController @RequestMapping("/auth")
public class AuthController {

    private final LoginUserUseCase loginUseCase;
    private final RegisteUserUseCase registerUseCase;
    private final TFG_Logger logger = new TFG_Logger();

    public AuthController(LoginUserUseCase loginUseCase, RegisteUserUseCase registerUseCase) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        RegisterResponse response = registerUseCase.register(req);
        logger.Info("New user registered: " + req.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        LoginResponse token = loginUseCase.login(req);
        logger.Info("Driver logged in: " + req.getEmail());
        return ResponseEntity.ok(token);
    }
}
