package com.sawah.sawah_backend.service.placePrice;

import com.sawah.sawah_backend.enums.VisitorCategoryAr;
import com.sawah.sawah_backend.enums.VisitorCategoryEn;
import com.sawah.sawah_backend.enums.VisitorNationalityAr;
import com.sawah.sawah_backend.enums.VisitorNationalityEn;
import com.sawah.sawah_backend.models.PlacePrice;

import java.math.BigDecimal;
import java.util.List;

public interface PlacePriceService {

    PlacePrice getById(Long id);

    List<PlacePrice> getByPlaceId(Long placeId);

    void create(
            VisitorCategoryEn visitorCategoryEn,
            VisitorNationalityEn visitorNationalityEn,
            VisitorCategoryAr visitorCategoryAr,
            VisitorNationalityAr visitorNationalityAr,
            BigDecimal price,
            Long placeId
    );

    void update(
            Long id,
            VisitorCategoryEn visitorCategoryEn,
            VisitorNationalityEn visitorNationalityEn,
            VisitorCategoryAr visitorCategoryAr,
            VisitorNationalityAr visitorNationalityAr,
            BigDecimal price
    );

    void delete(Long id);
}
