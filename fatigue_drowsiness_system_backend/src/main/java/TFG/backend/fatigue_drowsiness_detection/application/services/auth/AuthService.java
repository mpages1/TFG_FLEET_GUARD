package TFG.backend.fatigue_drowsiness_detection.application.services.auth;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import TFG.backend.fatigue_drowsiness_detection.application.in.driver.LoginUserUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.in.driver.RegisteUserUseCase;
import TFG.backend.fatigue_drowsiness_detection.application.out.AdministratorRepository;
import TFG.backend.fatigue_drowsiness_detection.application.out.DriverRepository;
import TFG.backend.fatigue_drowsiness_detection.application.out.RoleRepository;
import TFG.backend.fatigue_drowsiness_detection.application.out.UserRepository;
import TFG.backend.fatigue_drowsiness_detection.exceptions.UserAlreadyExistsException;
import TFG.backend.fatigue_drowsiness_detection.exceptions.UserNotFoundException;
import TFG.backend.fatigue_drowsiness_detection.logging.TFG_Logger;
import TFG.backend.fatigue_drowsiness_detection.model.adminsitrator.Administrator;
import TFG.backend.fatigue_drowsiness_detection.model.auth.LoginRequest;
import TFG.backend.fatigue_drowsiness_detection.model.auth.LoginResponse;
import TFG.backend.fatigue_drowsiness_detection.model.auth.RegisterRequest;
import TFG.backend.fatigue_drowsiness_detection.model.auth.RegisterResponse;
import TFG.backend.fatigue_drowsiness_detection.model.driver.Driver;
import TFG.backend.fatigue_drowsiness_detection.model.user.UserModel;
import TFG.backend.fatigue_drowsiness_detection.security.JwtTokenProvider;

@Service
public class AuthService implements RegisteUserUseCase, LoginUserUseCase {

    private final UserRepository userRepo;
    private final DriverRepository driverRepo;
    private final AdministratorRepository adminRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtUtil;

    private final TFG_Logger logger = new TFG_Logger();

    public AuthService(UserRepository UserRepo, PasswordEncoder encoder, JwtTokenProvider jwtUtil,
            DriverRepository driverRepo, AdministratorRepository adminRepo, RoleRepository roleRepo) {
        this.userRepo = UserRepo;
        this.driverRepo = driverRepo;
        this.adminRepo = adminRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public RegisterResponse register(RegisterRequest req) {

        req.validateByRole();

        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email Already Exists");
        }

        if (driverRepo.existsByLicenseNumber(req.getLicenseNumber())) {
            throw new UserAlreadyExistsException("License Number Already Exists");
        }

        UserModel user = new UserModel();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoleId(req.getRoleId());
        user.setCreatedAt(LocalDate.now());
        UserModel userSaved = userRepo.save(user);
        logger.Info("User saved: " + userSaved.getEmail() + " with ID: " + userSaved.getId());
        
        if (req.getRoleId() == 1) {
            Administrator admin = new Administrator();
            admin.setUserId(userSaved.getId());
            admin.setName(req.getName());
            admin.setPhone(req.getPhone());
            admin.setDepartment(req.getDepartment());
            adminRepo.save(admin);
        }
        else if (req.getRoleId() == 2) {
            Driver driver = new Driver();
            driver.setUserId(userSaved.getId());
            driver.setName(req.getName());
            driver.setPhone(req.getPhone());
            driver.setLicenseNumber(req.getLicenseNumber());
            driver.setDateOfBirth(req.getDateOfBirth());
            driver.setAddress(req.getAddress());
            driverRepo.save(driver);
        }

        return new RegisterResponse("User registered successfully");
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        UserModel user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contrasenya incorrecta");
        }

        LoginResponse response = new LoginResponse();
        response.setToken(jwtUtil.generateToken(user.getEmail()));
        response.setRole(roleRepo.getRoleName(user.getRoleId()));
        response.setId(user.getId());
        return response;
    }

}
