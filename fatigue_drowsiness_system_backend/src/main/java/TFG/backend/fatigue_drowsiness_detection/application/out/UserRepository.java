package TFG.backend.fatigue_drowsiness_detection.application.out;

import java.util.Optional;

import TFG.backend.fatigue_drowsiness_detection.model.user.UserModel;

public interface UserRepository {

    Optional<UserModel> findByEmail(String email);

    UserModel save(UserModel user);

    Optional<UserModel> findById(Integer id);

}
