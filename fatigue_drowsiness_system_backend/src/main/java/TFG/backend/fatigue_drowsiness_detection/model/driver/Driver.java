package TFG.backend.fatigue_drowsiness_detection.model.driver;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author mpages1
 */
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Driver {

    private Integer id;
    private Integer userId;
    private String name;
    private String phone;
    private String licenseNumber;
    private LocalDate dateOfBirth;
    private String address;
}
