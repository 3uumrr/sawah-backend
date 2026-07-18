package com.sawah.sawah_backend.dto.providerReview;

import java.time.LocalDateTime;

public record ProviderReviewResponseDto(
        Long id,
        Integer stars,
        String comment,
        LocalDateTime createdAt,
        String touristName,
        String touristImage
) {
}
