package com.sawah.sawah_backend.dto.placePrice;

import com.sawah.sawah_backend.enums.VisitorCategoryAr;
import com.sawah.sawah_backend.enums.VisitorCategoryEn;
import com.sawah.sawah_backend.enums.VisitorNationalityAr;
import com.sawah.sawah_backend.enums.VisitorNationalityEn;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PlacePriceInputDto(
        @NotNull(message = "place.price.visitor.category.en.not.null")
        VisitorCategoryEn visitorCategoryEn,

        @NotNull(message = "place.price.visitor.nationality.en.not.null")
        VisitorNationalityEn visitorNationalityEn,

        @NotNull(message = "place.price.visitor.category.ar.not.null")
        VisitorCategoryAr visitorCategoryAr,

        @NotNull(message = "place.price.visitor.nationality.ar.not.null")
        VisitorNationalityAr visitorNationalityAr,

        @NotNull(message = "place.price.not.null")
        @Positive(message = "place.price.positive")
        BigDecimal price
) {
}
