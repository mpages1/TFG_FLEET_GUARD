package TFG.backend.fatigue_drowsiness_detection.model.user;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserModel {
    private Integer id;
    private String email;
    private Integer roleId;
    private String password;
    private LocalDate createdAt;
    private LocalDate updatedAt;

}
