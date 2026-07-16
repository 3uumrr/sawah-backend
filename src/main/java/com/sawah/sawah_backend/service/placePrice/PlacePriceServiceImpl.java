package com.sawah.sawah_backend.service.placePrice;

import com.sawah.sawah_backend.enums.VisitorCategoryAr;
import com.sawah.sawah_backend.enums.VisitorCategoryEn;
import com.sawah.sawah_backend.enums.VisitorNationalityAr;
import com.sawah.sawah_backend.enums.VisitorNationalityEn;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.Place;
import com.sawah.sawah_backend.models.PlacePrice;
import com.sawah.sawah_backend.repository.PlacePriceRepository;
import com.sawah.sawah_backend.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlacePriceServiceImpl implements PlacePriceService {

    private final PlacePriceRepository placePriceRepository;
    private final PlaceRepository placeRepository;

    @Override
    public PlacePrice getById(Long id) {
        return placePriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("place.price.not.found"));
    }

    @Override
    public List<PlacePrice> getByPlaceId(Long placeId) {
        return placePriceRepository.findByPlaceId(placeId);
    }

    @Override
    @Transactional
    public void create(
            VisitorCategoryEn visitorCategoryEn,
            VisitorNationalityEn visitorNationalityEn,
            VisitorCategoryAr visitorCategoryAr,
            VisitorNationalityAr visitorNationalityAr,
            BigDecimal price,
            Long placeId
    ) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("place.not.found"));

        if (placePriceRepository.existsByPlaceIdAndVisitorCategoryEnAndVisitorNationalityEnAndVisitorCategoryArAndVisitorNationalityAr(
                placeId,
                visitorCategoryEn,
                visitorNationalityEn,
                visitorCategoryAr,
                visitorNationalityAr
        )) {
            throw new RuntimeException("place.price.already.exists");
        }

        PlacePrice placePrice = PlacePrice.builder()
                .visitorCategoryEn(visitorCategoryEn)
                .visitorNationalityEn(visitorNationalityEn)
                .visitorCategoryAr(visitorCategoryAr)
                .visitorNationalityAr(visitorNationalityAr)
                .price(price)
                .place(place)
                .build();

        placePriceRepository.save(placePrice);
    }

    @Override
    @Transactional
    public void update(
            Long id,
            VisitorCategoryEn visitorCategoryEn,
            VisitorNationalityEn visitorNationalityEn,
            VisitorCategoryAr visitorCategoryAr,
            VisitorNationalityAr visitorNationalityAr,
            BigDecimal price
    ) {
        PlacePrice placePrice = getById(id);

        placePriceRepository.findByPlaceIdAndVisitorCategoryEnAndVisitorNationalityEnAndVisitorCategoryArAndVisitorNationalityAr(
                placePrice.getPlace().getId(),
                visitorCategoryEn,
                visitorNationalityEn,
                visitorCategoryAr,
                visitorNationalityAr
        ).ifPresent(existingPlacePrice -> {
            if (!existingPlacePrice.getId().equals(placePrice.getId())) {
                throw new RuntimeException("place.price.already.exists");
            }
        });

        placePrice.setVisitorCategoryEn(visitorCategoryEn);
        placePrice.setVisitorNationalityEn(visitorNationalityEn);
        placePrice.setVisitorCategoryAr(visitorCategoryAr);
        placePrice.setVisitorNationalityAr(visitorNationalityAr);
        placePrice.setPrice(price);

        placePriceRepository.save(placePrice);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PlacePrice placePrice = getById(id);

        placePriceRepository.delete(placePrice);
    }
}
