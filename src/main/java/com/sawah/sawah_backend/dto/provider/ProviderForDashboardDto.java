package com.sawah.sawah_backend.dto.provider;

import java.math.BigDecimal;

public record ProviderForDashboardDto(
        Long providerId,
        String fullName,
        String providerImgUrl,
        String serviceNameAr,
        String serviceNameEn,
        Boolean isAvailable,
        BigDecimal averageRating,
        Integer totalReviews,
        Integer totalBookings
) {
}
