package TFG.backend.fatigue_drowsiness_detection.model.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class RoleModel {
    private Integer id;
    private String name;
}
