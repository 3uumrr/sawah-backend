package com.sawah.sawah_backend.service.review;

import com.sawah.sawah_backend.dto.review.ReviewInputDto;
import com.sawah.sawah_backend.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ReviewService {

    Review getById(Long id);

    Page<Review> getByPlaceId(Long placeId, Pageable pageable);

    Page<Review> getByUserId(Long userId, Pageable pageable);

    Map<String, Integer> getStarDistributionByPlaceId(Long placeId);

    Review getByUserIdAndPlaceId(Long userId, Long placeId);

    void create(ReviewInputDto reviewInputDto, Long placeId, Long userId);

    void update(Long id, Integer stars, String content, Long userId);

    void delete(Long id, Long userId);
}
