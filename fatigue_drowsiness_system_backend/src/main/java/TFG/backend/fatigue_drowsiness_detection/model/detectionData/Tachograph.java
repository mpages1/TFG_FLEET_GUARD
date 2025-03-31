package TFG.backend.fatigue_drowsiness_detection.model.detectionData;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class Tachograph {

    private Integer id;

    private Integer driverId;

    private Date timestamp;

    private Float drivingTime;

    private Float drivingDistance;

    private Float speed;

    private Float breakTime;

    private Float engineHours;

    private Float fuelConsumption;

    private Float rpm;

    private Integer gearPosition;

    private Boolean brakePedal;

    private Boolean acceleratorPedal;

    private Float steeringWheelAngle;

    private Boolean driverActive;

}
