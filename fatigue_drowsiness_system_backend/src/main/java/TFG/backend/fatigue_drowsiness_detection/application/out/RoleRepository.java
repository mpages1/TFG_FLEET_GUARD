package TFG.backend.fatigue_drowsiness_detection.application.out;

import TFG.backend.fatigue_drowsiness_detection.model.role.RoleModel;

public interface RoleRepository {
    void save(RoleModel role);
    String getRoleName(Integer id);
}
