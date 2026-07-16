package com.sawah.sawah_backend.service.review;

import com.sawah.sawah_backend.dto.review.ReviewInputDto;
import com.sawah.sawah_backend.exceptions.ForbiddenException;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.Place;
import com.sawah.sawah_backend.models.Review;
import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.repository.PlaceRepository;
import com.sawah.sawah_backend.repository.ReviewRepository;
import com.sawah.sawah_backend.repository.UserRepository;
import com.sawah.sawah_backend.service.place.PlaceService;
import com.sawah.sawah_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final UserService userService;

    private Place getByIdOrThrow(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("place.not.found"));
    }

    @Override
    public Review getById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("review.not.found"));
    }

    @Override
    public Page<Review> getByPlaceId(Long placeId, Pageable pageable) {

        Place place = getByIdOrThrow(placeId);

        return reviewRepository.findByPlaceIdOrderByCreatedAtDesc(place.getId(), pageable);

    }

    @Override
    public Page<Review> getByUserId(Long userId, Pageable pageable) {
        User user = userService.getUserById(userId);

        return reviewRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);
    }

    @Override
    public Map<String, Integer> getStarDistributionByPlaceId(Long placeId) {
        Place place = getByIdOrThrow(placeId);
        Map<String, Integer> stars = new HashMap<>();
        stars.put("one", reviewRepository.countByPlaceIdAndStars(place.getId(), 1));
        stars.put("two", reviewRepository.countByPlaceIdAndStars(place.getId(), 2));
        stars.put("three", reviewRepository.countByPlaceIdAndStars(place.getId(), 3));
        stars.put("four", reviewRepository.countByPlaceIdAndStars(place.getId(), 4));
        stars.put("five", reviewRepository.countByPlaceIdAndStars(place.getId(), 5));
        return stars;
    }

    @Override
    public Review getByUserIdAndPlaceId(Long userId, Long placeId) {

        User user = userService.getUserById(userId);

        Place place = getByIdOrThrow(placeId);

        return reviewRepository.findByUserIdAndPlaceId(user.getId(), place.getId())
                .orElseThrow(() -> new ResourceNotFoundException("review.not.found"));
    }

    @Override
    @Transactional
    public void create(ReviewInputDto reviewInputDto, Long placeId, Long userId) {
        validateStars(reviewInputDto.stars());

        Place place = getByIdOrThrow(placeId);

        User user = userService.getUserById(userId);

        if (reviewRepository.existsByUserIdAndPlaceId(userId, placeId)) {
            throw new RuntimeException("review.already.exists");
        }

        Review review = Review.builder()
                .stars(reviewInputDto.stars())
                .content(reviewInputDto.content())
                .place(place)
                .user(user)
                .build();

        reviewRepository.save(review);
        recalculatePlaceRating(placeId);
    }

    @Override
    @Transactional
    public void update(Long id, Integer stars, String content, Long userId) {
        validateStars(stars);

        Review review = getById(id);
        validateReviewOwner(review, userId);
        Long placeId = review.getPlace().getId();

        review.setStars(stars);
        review.setContent(content);

        reviewRepository.save(review);
        recalculatePlaceRating(placeId);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        Review review = getById(id);
        validateReviewOwner(review, userId);
        Long placeId = review.getPlace().getId();

        reviewRepository.delete(review);
        recalculatePlaceRating(placeId);
    }

    private void validateStars(Integer stars) {
        if (stars == null || stars < 1 || stars > 5) {
            throw new RuntimeException("review.stars.invalid");
        }
    }

    private void validateReviewOwner(Review review, Long userId) {
        if (review.getUser() == null || !Objects.equals(review.getUser().getId(), userId)) {
            throw new ForbiddenException("review.not.owner");
        }
    }

    private void recalculatePlaceRating(Long placeId) {
        Place place = getByIdOrThrow(placeId);

        long totalReviews = reviewRepository.countByPlaceId(placeId);
        Double averageStars = reviewRepository.findAverageStarsByPlaceId(placeId);
        BigDecimal rating = averageStars == null
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(averageStars).setScale(2, RoundingMode.HALF_UP);

        place.setTotalReviews(Math.toIntExact(totalReviews));
        place.setRating(rating);

        placeRepository.save(place);
    }
}
