package com.sawah.sawah_backend.dto.provider;

import java.math.BigDecimal;

public record ProviderWithoutDetailsResponseDto(
        Long id,
        String fullName,
        String providerImgUrl,
        String bio,
        BigDecimal ratePerHour,
        BigDecimal ratePerDay,
        BigDecimal averageRating,
        Integer totalReviews
) {
}
