package com.sawah.sawah_backend.dto.placePrice;

import com.sawah.sawah_backend.enums.VisitorCategoryAr;
import com.sawah.sawah_backend.enums.VisitorCategoryEn;
import com.sawah.sawah_backend.enums.VisitorNationalityAr;
import com.sawah.sawah_backend.enums.VisitorNationalityEn;
import java.math.BigDecimal;

public record PlacePriceResponseDto(
        Long id,
        VisitorCategoryEn visitorCategoryEn,
        VisitorNationalityEn visitorNationalityEn,
        VisitorCategoryAr visitorCategoryAr,
        VisitorNationalityAr visitorNationalityAr,
        BigDecimal price
) {
}
