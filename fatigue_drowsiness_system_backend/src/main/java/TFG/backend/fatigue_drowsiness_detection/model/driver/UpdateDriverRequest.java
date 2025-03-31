package TFG.backend.fatigue_drowsiness_detection.model.driver;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateDriverRequest {
    private String name;
    private String phone;
    private String licenseNumber;
    private LocalDate dateOfBirth;
    private String address;
}
