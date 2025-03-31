package TFG.backend.fatigue_drowsiness_detection.adapters.out.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataRoleRepositoy extends JpaRepository<RoleEntity, Integer> {

    @Query("SELECT r FROM RoleEntity r WHERE r.name = :name")
    RoleEntity findByName(String name);

}
