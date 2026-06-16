package com.sawah.sawah_backend.dto.recommendation;

import java.util.List;
import java.util.Set;

public record RecommendationResponseDto(
        List<Long> recommendedPlaceIds
) {
}
