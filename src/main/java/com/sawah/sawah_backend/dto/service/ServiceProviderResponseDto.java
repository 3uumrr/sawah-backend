package com.sawah.sawah_backend.dto.service;

import com.sawah.sawah_backend.dto.driverProfile.DriverProfileResponseDto;
import com.sawah.sawah_backend.dto.providerLanguage.ProviderLanguageResponseDto;

import java.util.List;

public record ServiceProviderResponseDto(

        DriverProfileResponseDto driverProfile,
        List<ProviderLanguageResponseDto> providerLanguages
) {
}
