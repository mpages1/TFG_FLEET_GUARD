package TFG.backend.fatigue_drowsiness_detection.adapters.out.administrator;

import TFG.backend.fatigue_drowsiness_detection.model.adminsitrator.Administrator;

final class AdministratorMapper {

    private AdministratorMapper() {
    }

    static Administrator toModel(AdministratorEntity administrator) {
        return Administrator.builder()
                .id(administrator.getId())
                .userId(administrator.getUserId())
                .name(administrator.getName())
                .department(administrator.getDepartment()).phone(administrator.getPhone())
                .department(administrator.getDepartment()).build();
    }

    static AdministratorEntity toEntity(Administrator administrator) {
        return AdministratorEntity.builder()
                .id(administrator.getId())
                .userId(administrator.getUserId())
                .name(administrator.getName())
                .phone(administrator.getPhone())
                .department(administrator.getDepartment())
                .build();
    }
}
