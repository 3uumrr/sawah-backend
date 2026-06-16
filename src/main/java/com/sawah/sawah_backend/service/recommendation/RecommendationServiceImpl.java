package com.sawah.sawah_backend.service.recommendation;

import com.sawah.sawah_backend.dto.recommendation.RecommendationRequestDto;
import com.sawah.sawah_backend.dto.recommendation.RecommendationResponseDto;
import com.sawah.sawah_backend.models.Place;
import com.sawah.sawah_backend.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final WebClient webClient;
    private final PlaceRepository placeRepository;

    @Override
    public List<Long> getRecommendations(RecommendationRequestDto request) {

        RecommendationResponseDto response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/recommend")
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(RecommendationResponseDto.class)
                .block();

        if (response == null || response.recommendedPlaceIds() == null || response.recommendedPlaceIds().isEmpty()) {
            return List.of();
        }

        return response.recommendedPlaceIds();
    }
}
