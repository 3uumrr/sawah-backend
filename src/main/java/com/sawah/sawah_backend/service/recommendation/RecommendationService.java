package com.sawah.sawah_backend.service.recommendation;

import com.sawah.sawah_backend.dto.recommendation.RecommendationRequestDto;
import com.sawah.sawah_backend.models.Place;

import java.util.List;
import java.util.Set;

public interface RecommendationService {

    List<Long> getRecommendations(RecommendationRequestDto request);
}
