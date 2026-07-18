package com.sawah.sawah_backend.dto.provider;

import com.sawah.sawah_backend.dto.driverProfile.UpdateDriverVehicleDto;
import com.sawah.sawah_backend.dto.providerLanguage.UpdateProviderLanguageDto;

import java.math.BigDecimal;
import java.util.List;

public record ProviderProfileResponseDto(
        String firstName,
        String lastName,
        String email,
        String providerImgUrl,
        String phoneNumber,
        String country,
        String gender,
        String bio,
        Integer experienceYears,
        BigDecimal ratePerHour,
        BigDecimal ratePerDay,
        List<UpdateProviderLanguageDto> languages,
        UpdateDriverVehicleDto vehicleDetails
) {
}
