package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.enums.VisitorCategoryAr;
import com.sawah.sawah_backend.enums.VisitorCategoryEn;
import com.sawah.sawah_backend.enums.VisitorNationalityAr;
import com.sawah.sawah_backend.enums.VisitorNationalityEn;
import com.sawah.sawah_backend.models.PlacePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlacePriceRepository extends JpaRepository<PlacePrice, Long> {

    List<PlacePrice> findByPlaceId(Long placeId);

    Optional<PlacePrice> findByPlaceIdAndVisitorCategoryEnAndVisitorNationalityEnAndVisitorCategoryArAndVisitorNationalityAr(
            Long placeId,
            VisitorCategoryEn visitorCategoryEn,
            VisitorNationalityEn visitorNationalityEn,
            VisitorCategoryAr visitorCategoryAr,
            VisitorNationalityAr visitorNationalityAr
    );

    boolean existsByPlaceIdAndVisitorCategoryEnAndVisitorNationalityEnAndVisitorCategoryArAndVisitorNationalityAr(
            Long placeId,
            VisitorCategoryEn visitorCategoryEn,
            VisitorNationalityEn visitorNationalityEn,
            VisitorCategoryAr visitorCategoryAr,
            VisitorNationalityAr visitorNationalityAr
    );
}
