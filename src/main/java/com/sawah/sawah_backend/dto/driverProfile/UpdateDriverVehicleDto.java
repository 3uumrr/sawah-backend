package com.sawah.sawah_backend.dto.driverProfile;

import com.sawah.sawah_backend.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDriverVehicleDto(
                Long id,
                VehicleType vehicleType,

                @Size(min = 2, max = 200, message = "driver.vehicleModel.size")
                String vehicleModel) {
}
