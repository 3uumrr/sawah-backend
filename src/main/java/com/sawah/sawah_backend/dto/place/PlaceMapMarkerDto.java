package com.sawah.sawah_backend.dto.place;

import java.math.BigDecimal;

public record PlaceMapMarkerDto(
        Long id,
        String nameAr,
        String nameEn,
        BigDecimal longitude,
        BigDecimal latitude
) {
}
