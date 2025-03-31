package TFG.backend.fatigue_drowsiness_detection.adapters.out.user;

import TFG.backend.fatigue_drowsiness_detection.model.user.UserModel;

final class UserMapper {

    public static UserEntity toEntity(UserModel userModel) {
        UserEntity userEntity = UserEntity.builder()
                .id(userModel.getId())
                .email(userModel.getEmail())
                .roleId(userModel.getRoleId())
                .password(userModel.getPassword())
                .createdAt(userModel.getCreatedAt())
                .updatedAt(userModel.getUpdatedAt())
                .build();
        return userEntity;
    }

    public static UserModel toModel(UserEntity userEntity) {
        UserModel userModel = UserModel.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .roleId(userEntity.getRoleId())
                .password(userEntity.getPassword())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
        return userModel;
    }

}
