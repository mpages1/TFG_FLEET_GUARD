package TFG.backend.fatigue_drowsiness_detection.adapters.out.driver;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author mpages1
 */
@Repository
public interface SpringDataDriverRepository extends JpaRepository<DriverEntity, Integer> {

	@Query("SELECT d FROM DriverEntity d WHERE d.id = :id")
	Optional<DriverEntity> findById(Integer id);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DriverEntity d WHERE d.licenseNumber = :licenseNumber")
    boolean existsByLicenseNumber(String licenseNumber);

    @Query("SELECT d FROM DriverEntity d WHERE d.userId = :userId")
    Optional<DriverEntity> findByUserId(Integer userId);
}
