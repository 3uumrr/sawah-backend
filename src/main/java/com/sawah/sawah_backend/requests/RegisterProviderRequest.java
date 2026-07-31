package com.sawah.sawah_backend.requests;

import com.sawah.sawah_backend.enums.ServiceCode;
import com.sawah.sawah_backend.enums.VehicleType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record RegisterProviderRequest(
        @NotBlank(message = "provider.nationalId.required")
        String nationalId,

        @NotNull(message = "provider.serviceCode.required")
        ServiceCode serviceCode,

        @NotNull(message = "provider.experienceYears.required")
        Integer experienceYears,

        @NotNull(message = "provider.ratePerHour.required")
        BigDecimal ratePerHour,

        @NotNull(message = "provider.ratePerDay.required")
        BigDecimal ratePerDay,

        List<@Valid ProviderLanguageRequest> languageRequest,

        VehicleType vehicleType,

        String vehicleModel
) {
}
