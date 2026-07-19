package com.sawah.sawah_backend.dto.place;

import java.math.BigDecimal;

public record PlaceWithoutDetailsResponseDto(
        Long id,
        String nameEn,
        String nameAr,
        String governorateAr,
        String governorateEn,
        String mainImageUrl,
        BigDecimal averageRating,
        Integer totalReviews,
        boolean isFavorite
) {
}
