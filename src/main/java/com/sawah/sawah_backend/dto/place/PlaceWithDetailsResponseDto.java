package com.sawah.sawah_backend.dto.place;

import com.sawah.sawah_backend.dto.placePhoto.PlacePhotoResponseDto;
import com.sawah.sawah_backend.dto.placePrice.PlacePriceResponseDto;
import com.sawah.sawah_backend.dto.review.ReviewResponseDto;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public record PlaceWithDetailsResponseDto(
        Long id,
        String nameEn,
        String nameAr,
        String governorateAr,
        String governorateEn,
        BigDecimal averageRating,
        Integer totalReviews,
        String descriptionAr,
        String descriptionEn,
        LocalTime openTime,
        LocalTime closeTime,
        BigDecimal longitude,
        BigDecimal latitude,
        String bookingUrl,
        boolean isFavorite,
        boolean isVisited,

        Map<String,Integer> numOfStars,
        List<PlacePhotoResponseDto> photos,
        List<PlacePriceResponseDto> prices,
        List<ReviewResponseDto> reviews
) {
}
