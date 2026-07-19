package com.sawah.sawah_backend.dto.recommendation;

import java.math.BigDecimal;

public record PlaceFeatureDto(
        Long id,
        String category,
        BigDecimal averageRating
) {
}
