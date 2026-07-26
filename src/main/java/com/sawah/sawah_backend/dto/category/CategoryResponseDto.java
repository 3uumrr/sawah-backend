package com.sawah.sawah_backend.dto.category;

import java.time.LocalDateTime;

public record CategoryResponseDto(
        Long id,

        String nameAr,

        String nameEn,

        String iconUrl,
        Integer displayOrder,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
