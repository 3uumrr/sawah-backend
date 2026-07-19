package com.sawah.sawah_backend.dto.place;

import com.sawah.sawah_backend.dto.placePrice.PlacePriceInputDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public record PlaceInputDto(
        @NotBlank(message = "place.nameAr.required")
        @Size(max = 50, message = "place.nameAr.size")
        String nameAr,

        @NotBlank(message = "place.nameEn.required")
        @Size(max = 50, message = "place.nameEn.size")
        String nameEn,

        @NotBlank(message = "place.governorateAr.required")
        @Size(max = 50, message = "place.governorateAr.size")
        String governorateAr,

        @NotBlank(message = "place.governorateEn.required")
        @Size(max = 50, message = "place.governorateEn.size")
        String governorateEn,

        @NotBlank(message = "place.descriptionAr.required")
        @Size(max = 2000, message = "place.descriptionAr.size")
        String descriptionAr,

        @NotBlank(message = "place.descriptionEn.required")
        @Size(max = 2000, message = "place.descriptionEn.size")
        String descriptionEn,

        LocalTime openTime,

        LocalTime closeTime,

        @NotNull(message = "place.longitude.required")
        BigDecimal longitude,

        @NotNull(message = "place.latitude.required")
        BigDecimal latitude,

        @Size(max = 500, message = "place.bookingUrl.size")
        String bookingUrl,

        @NotNull(message = "place.categoryId.required")
        Long categoryId,

        List<@Valid PlacePriceInputDto> prices
) {
}
