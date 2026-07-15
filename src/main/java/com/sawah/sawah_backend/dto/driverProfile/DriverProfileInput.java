package com.sawah.sawah_backend.dto.driverProfile;

import com.sawah.sawah_backend.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DriverProfileInput(
        @NotBlank(message = "driver.vehicleType.required")
        @Size(min = 2, max = 20, message = "driver.vehicleType.size")
        VehicleType vehicleType,

        @NotBlank(message = "driver.vehicleModel.required")
        @Size(min = 2, max = 200, message = "driver.vehicleModel.size")
        String vehicleModel
)
{}
