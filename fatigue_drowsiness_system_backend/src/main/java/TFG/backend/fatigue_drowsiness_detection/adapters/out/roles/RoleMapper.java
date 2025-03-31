package TFG.backend.fatigue_drowsiness_detection.adapters.out.roles;

import TFG.backend.fatigue_drowsiness_detection.model.role.RoleModel;

final class RoleMapper {

    private RoleMapper() {
    }

    static RoleModel toModel(RoleEntity roleEntity) {
        return RoleModel.builder().
                id(roleEntity.getId())
                .name(roleEntity.getName())
                .build();
    }

    static RoleEntity toEntity(RoleModel role) {
        return RoleEntity.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
