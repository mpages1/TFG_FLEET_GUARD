package TFG.backend.fatigue_drowsiness_detection.model.auth;

import java.time.LocalDate;

import TFG.backend.fatigue_drowsiness_detection.exceptions.RegisterFieldRequiredException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class RegisterRequest {

    private String name;

    private String email;

    private String password;

    private Integer roleId;

    private String licenseNumber;

    private String phone;

    private LocalDate dateOfBirth;

    private String address;

    private String department;

    public void validateByRole() {
        if (roleId == null) {
            throw new RegisterFieldRequiredException("Role ID is required.");
        }

        if (name == null || name.isBlank()) {
            throw new RegisterFieldRequiredException("Name is required.");
        }

        if (email == null || email.isBlank()) {
            throw new RegisterFieldRequiredException("Email is required.");
        }

        if (password == null || password.isBlank()) {
            throw new RegisterFieldRequiredException("Password is required.");
        }

        if (phone == null || phone.isBlank()) {
            throw new RegisterFieldRequiredException("Phone is required.");
        }

        if (roleId == 1) {
            if (department == null || department.isBlank()) {
                throw new RegisterFieldRequiredException("Department is required for Administrator.");
            }
        }

        if (roleId == 2) {
            if (licenseNumber == null || licenseNumber.isBlank()) {
                throw new RegisterFieldRequiredException("License number is required for Driver.");
            }
            if (dateOfBirth == null) {
                throw new RegisterFieldRequiredException("Date of birth is required for Driver.");
            }
            if (address == null || address.isBlank()) {
                throw new RegisterFieldRequiredException("Address is required for Driver.");
            }
            if (phone == null || phone.isBlank()) {
                throw new RegisterFieldRequiredException("Phone is required for Driver.");
            }
        }
    }

}
