package TFG.backend.fatigue_drowsiness_detection.model.adminsitrator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class Administrator {
    private Integer id;
    private Integer userId;
    private String name;
    private String phone;
    private String department;
}
