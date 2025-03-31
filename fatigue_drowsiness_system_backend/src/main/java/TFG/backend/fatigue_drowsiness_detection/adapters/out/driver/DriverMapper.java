package TFG.backend.fatigue_drowsiness_detection.adapters.out.driver;

import TFG.backend.fatigue_drowsiness_detection.model.driver.Driver;

final class DriverMapper {

    private DriverMapper() {
    }

    static DriverEntity toEntity(Driver driver) {
        DriverEntity driverEntity = DriverEntity.builder()
                .id(driver.getId())
                .userId(driver.getUserId())
                .name(driver.getName())
                .address(driver.getAddress())
                .phone(driver.getPhone())
                .licenseNumber(driver.getLicenseNumber())
                .dateOfBirth(driver.getDateOfBirth())
                .build();
        return driverEntity;
    }

    static Driver toModel(DriverEntity driverEntity) {
        Driver driver = Driver.builder()
                .id(driverEntity.getId())
                .userId(driverEntity.getUserId())
                .name(driverEntity.getName())
                .address(driverEntity.getAddress())
                .phone(driverEntity.getPhone())
                .licenseNumber(driverEntity.getLicenseNumber())
                .dateOfBirth(driverEntity.getDateOfBirth())
                .build();
        return driver;
    }

}
