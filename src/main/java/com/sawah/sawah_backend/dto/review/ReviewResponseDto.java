package com.sawah.sawah_backend.dto.review;

import java.time.LocalDateTime;

public record ReviewResponseDto(
        Long id,
        Integer stars,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String touristName,
        String touristImage,
        Boolean isOwnReview
) {
}
