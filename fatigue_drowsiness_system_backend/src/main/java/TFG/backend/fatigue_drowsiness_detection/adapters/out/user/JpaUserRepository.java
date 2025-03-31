package TFG.backend.fatigue_drowsiness_detection.adapters.out.user;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import TFG.backend.fatigue_drowsiness_detection.application.out.UserRepository;
import TFG.backend.fatigue_drowsiness_detection.model.user.UserModel;

@Repository
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserRepository repository;

    public JpaUserRepository(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        
        UserEntity userEntity = repository.findByEmail(email);
        if (userEntity == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(UserMapper.toModel(repository.findByEmail(email)));
    }

    @Override
    public UserModel save(UserModel user) {
        user.setUpdatedAt(LocalDate.now());
        UserEntity userEntity = repository.save(UserMapper.toEntity(user));
        return UserMapper.toModel(userEntity);
    }

    @Override
    public Optional<UserModel> findById(Integer id) {
        Optional<UserEntity> userEntity = repository.findById(id);
        if (userEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(UserMapper.toModel(userEntity.get()));
    }
}
