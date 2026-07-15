package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.driverProfile.DriverProfileInput;
import com.sawah.sawah_backend.dto.driverProfile.DriverProfileResponseDto;
import com.sawah.sawah_backend.dto.driverProfile.UpdateDriverVehicleDto;
import com.sawah.sawah_backend.enums.VehicleType;
import com.sawah.sawah_backend.models.DriverProfile;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DriverProfileMapper {

    @Mapping(target = "vehicleType", source = "vehicleType")
    DriverProfile toEntity(DriverProfileInput driverProfileInput);

    DriverProfileResponseDto toDto(DriverProfile driverProfile);

    void updateEntityFromDto(DriverProfileInput driverProfileInput , @MappingTarget DriverProfile driverProfile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "vehicleCapacity", ignore = true)
    DriverProfile toEntity(UpdateDriverVehicleDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "vehicleCapacity", ignore = true)
    void updateEntityFromDto(UpdateDriverVehicleDto dto, @MappingTarget DriverProfile driverProfile);

    @AfterMapping
    default void setCapacityFromVehicleType(@MappingTarget DriverProfile driverProfile) {
        if (driverProfile.getVehicleType() != null) {
            driverProfile.setVehicleCapacity(driverProfile.getVehicleType().getCapacity());
        }
    }

    default VehicleType mapStringToVehicleType(String vehicleType) {
        if (vehicleType == null) {
            return null;
        }
        try {
            return VehicleType.valueOf(vehicleType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("driver.vehicleType.invalid");
        }
    }

}
