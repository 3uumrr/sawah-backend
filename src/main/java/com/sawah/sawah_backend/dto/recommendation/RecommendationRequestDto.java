package com.sawah.sawah_backend.dto.recommendation;

import java.util.List;

public record RecommendationRequestDto(
        List<PlaceFeatureDto> allPlaces,
        List<PlaceFeatureDto> visitedPlaces,
        List<PlaceFeatureDto> favoritePlaces,
        List<String> preferredCategories
) {
}
