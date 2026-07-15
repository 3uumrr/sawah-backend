package com.sawah.sawah_backend.dto.driverProfile;

import com.sawah.sawah_backend.enums.VehicleType;

public record DriverProfileResponseDto(
        Long id,
     VehicleType vehicleType ,
     String vehicleModel,
     Integer vehicleCapacity
) {
}
