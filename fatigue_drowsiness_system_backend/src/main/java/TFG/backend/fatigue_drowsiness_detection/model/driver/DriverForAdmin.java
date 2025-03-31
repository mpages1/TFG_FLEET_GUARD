package TFG.backend.fatigue_drowsiness_detection.model.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class DriverForAdmin {
    private Integer id;
    private String name;
    private String phone;
    private String licenseNumber;
}
