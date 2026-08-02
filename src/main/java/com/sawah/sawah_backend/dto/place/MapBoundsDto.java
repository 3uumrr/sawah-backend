package com.sawah.sawah_backend.dto.place;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MapBoundsDto(
        @NotNull
        BigDecimal minLat,

        @NotNull
        BigDecimal maxLat,

        @NotNull
        BigDecimal minLng,

        @NotNull
        BigDecimal maxLng
) {
}
