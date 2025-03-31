package TFG.backend.fatigue_drowsiness_detection.adapters.out.administrator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprinDataAdministratorRepository extends JpaRepository<AdministratorEntity, Integer> {

}
