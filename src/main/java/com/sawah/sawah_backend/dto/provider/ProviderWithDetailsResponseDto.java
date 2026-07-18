package com.sawah.sawah_backend.dto.provider;

import com.sawah.sawah_backend.dto.service.ServiceProviderResponseDto;

import java.math.BigDecimal;

public record ProviderWithDetailsResponseDto(
        Long id,
        String fullName,
        String providerImgUrl,
        String bio,
        BigDecimal ratePerHour,
        BigDecimal ratePerDay,
        BigDecimal averageRating,
        Integer totalReviews,
        Integer experienceYears,

        ServiceProviderResponseDto service

) {
}
