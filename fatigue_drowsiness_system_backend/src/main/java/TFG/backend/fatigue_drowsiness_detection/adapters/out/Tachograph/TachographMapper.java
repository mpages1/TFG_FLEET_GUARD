package TFG.backend.fatigue_drowsiness_detection.adapters.out.Tachograph;

import TFG.backend.fatigue_drowsiness_detection.model.detectionData.Tachograph;

final class TachographMapper {

    private TachographMapper() {
    }

    static TachographEntity toEntity(Tachograph tachograph) {
        return TachographEntity.builder()
                .driverId(tachograph.getDriverId())
                .timestamp(tachograph.getTimestamp())
                .drivingTime(tachograph.getDrivingTime())
                .drivingDistance(tachograph.getDrivingDistance())
                .speed(tachograph.getSpeed())
                .breakTime(tachograph.getBreakTime())
                .engineHours(tachograph.getEngineHours())
                .fuelConsumption(tachograph.getFuelConsumption())
                .rpm(tachograph.getRpm())
                .gearPosition(tachograph.getGearPosition())
                .brakePedal(tachograph.getBrakePedal())
                .acceleratorPedal(tachograph.getAcceleratorPedal())
                .steeringWheelAngle(tachograph.getSteeringWheelAngle())
                .driverActive(tachograph.getDriverActive())
                .build();
    }

    static Tachograph toModel(TachographEntity tachographEntity) {
        return Tachograph.builder()
                .driverId(tachographEntity.getDriverId())
                .timestamp(tachographEntity.getTimestamp())
                .drivingTime(tachographEntity.getDrivingTime())
                .drivingDistance(tachographEntity.getDrivingDistance())
                .speed(tachographEntity.getSpeed())
                .breakTime(tachographEntity.getBreakTime())
                .engineHours(tachographEntity.getEngineHours())
                .fuelConsumption(tachographEntity.getFuelConsumption())
                .rpm(tachographEntity.getRpm())
                .gearPosition(tachographEntity.getGearPosition())
                .brakePedal(tachographEntity.getBrakePedal())
                .acceleratorPedal(tachographEntity.getAcceleratorPedal())
                .steeringWheelAngle(tachographEntity.getSteeringWheelAngle())
                .driverActive(tachographEntity.getDriverActive())
                .build();
    }

}
