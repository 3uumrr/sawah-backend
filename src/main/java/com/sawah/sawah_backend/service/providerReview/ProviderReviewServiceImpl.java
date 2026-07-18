package com.sawah.sawah_backend.service.providerReview;

import com.sawah.sawah_backend.dto.providerReview.ProviderReviewRequestDto;
import com.sawah.sawah_backend.enums.ServiceRequestStatus;
import com.sawah.sawah_backend.exceptions.BadRequestException;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.Provider;
import com.sawah.sawah_backend.models.ProviderReview;
import com.sawah.sawah_backend.models.ServiceRequest;
import com.sawah.sawah_backend.repository.ProviderReviewRepository;
import com.sawah.sawah_backend.repository.ServiceRequestRepository;
import com.sawah.sawah_backend.service.provider.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProviderReviewServiceImpl implements ProviderReviewService {

    private final ProviderReviewRepository providerReviewRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final ProviderService providerService;

    @Override
    public Page<ProviderReview> getByProviderId(Long providerId, Pageable pageable) {
        Provider provider = providerService.getProviderById(providerId);

        return providerReviewRepository.findAllByProviderId(provider.getId(), pageable);
    }

    @Override
    @Transactional
    public ProviderReview createForCompletedBooking(Long bookingId, Long touristId, ProviderReviewRequestDto requestDto) {
        ServiceRequest booking = serviceRequestRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("booking.not.found"));

        validateTouristOwnership(booking, touristId);
        validateCompletedBooking(booking);
        validateReviewDoesNotExist(booking.getId());

        ProviderReview providerReview = ProviderReview.builder()
                .stars(requestDto.stars())
                .comment(normalizeComment(requestDto.comment()))
                .tourist(booking.getTourist())
                .provider(booking.getProvider())
                .serviceRequest(booking)
                .build();

        ProviderReview savedProviderReview = providerReviewRepository.save(providerReview);
        updateProviderRatingStats(booking.getProvider());

        return savedProviderReview;
    }

    @Override
    @Transactional
    public void deleteByTouristId(Long touristId) {
        providerReviewRepository.deleteByTouristId(touristId);
    }

    private void validateTouristOwnership(ServiceRequest booking, Long touristId) {
        if (booking.getTourist() == null || !Objects.equals(booking.getTourist().getId(), touristId)) {
            throw new BadRequestException("provider.review.unauthorized.access");
        }
    }

    private void validateCompletedBooking(ServiceRequest booking) {
        if (booking.getStatus() != ServiceRequestStatus.COMPLETED) {
            throw new BadRequestException("provider.review.booking.not.completed");
        }
    }

    private void validateReviewDoesNotExist(Long bookingId) {
        if (providerReviewRepository.existsByServiceRequest_Id(bookingId)) {
            throw new BadRequestException("provider.review.already.exists");
        }
    }

    private String normalizeComment(String comment) {
        if (comment == null) {
            return null;
        }

        String trimmedComment = comment.trim();
        return trimmedComment.isEmpty() ? null : trimmedComment;
    }

    private void updateProviderRatingStats(Provider provider) {
        long totalReviews = providerReviewRepository.countByProvider_Id(provider.getId());
        Double averageStars = providerReviewRepository.findAverageStarsByProviderId(provider.getId());

        provider.setTotalReviews(Math.toIntExact(totalReviews));
        provider.setAverageRating(averageStars == null
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(averageStars).setScale(2, RoundingMode.HALF_UP));
    }
}
