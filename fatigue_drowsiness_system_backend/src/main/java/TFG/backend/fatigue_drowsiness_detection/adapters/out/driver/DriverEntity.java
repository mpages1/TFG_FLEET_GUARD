package TFG.backend.fatigue_drowsiness_detection.adapters.out.driver;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author mpages1
 */
@Entity
@Table(name = "drivers")
@Getter
@Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class DriverEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "license_number", unique = true, nullable = false)
    private String licenseNumber;

	@Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address")
	private String address;
}
