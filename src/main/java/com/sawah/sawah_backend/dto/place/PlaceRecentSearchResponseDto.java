package com.sawah.sawah_backend.dto.place;

public record PlaceRecentSearchResponseDto(
        Long id,
        String nameEn,
        String nameAr,
        String mainImageUrl
) {
}
