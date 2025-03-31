package TFG.backend.fatigue_drowsiness_detection.adapters.out.Tachograph;

import java.util.Date;

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

@Entity @Table(name = "tachograph_data") @Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class TachographEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "driver_id", nullable = false)
    private Integer driverId;

    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @Column(name = "driving_time", nullable = false)
    private Float drivingTime;

    @Column(name = "driving_distance", nullable = false)
    private Float drivingDistance;

    @Column(name = "speed", nullable = false)
    private Float speed;

    @Column(name = "break_time", nullable = false)
    private Float breakTime;

    @Column(name = "engine_hours", nullable = false)
    private Float engineHours;

    @Column(name = "fuel_consumption", nullable = false)
    private Float fuelConsumption;

    @Column(name = "rpm", nullable = false)
    private Float rpm;

    @Column(name = "gear_position", nullable = false)
    private Integer gearPosition;

    @Column(name = "brake_pedal", nullable = false)
    private Boolean brakePedal;

    @Column(name = "accelerator_pedal", nullable = false)
    private Boolean acceleratorPedal;

    @Column(name = "steering_wheel_angle", nullable = false)
    private Float steeringWheelAngle;

    @Column(name = "driver_active", nullable = false)
    private Boolean driverActive;

}
