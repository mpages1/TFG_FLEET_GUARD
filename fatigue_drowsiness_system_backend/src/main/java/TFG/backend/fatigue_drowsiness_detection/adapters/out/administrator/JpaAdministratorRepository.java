package TFG.backend.fatigue_drowsiness_detection.adapters.out.administrator;

import org.springframework.stereotype.Repository;

import TFG.backend.fatigue_drowsiness_detection.application.out.AdministratorRepository;
import TFG.backend.fatigue_drowsiness_detection.model.adminsitrator.Administrator;

@Repository
public class JpaAdministratorRepository implements AdministratorRepository {

    private final SprinDataAdministratorRepository repository;

    public JpaAdministratorRepository(SprinDataAdministratorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Administrator administrator) {
        repository.save(AdministratorMapper.toEntity(administrator));
    }
}
