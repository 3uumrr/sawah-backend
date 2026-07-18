package com.sawah.sawah_backend.service.providerReview;

import com.sawah.sawah_backend.dto.providerReview.ProviderReviewRequestDto;
import com.sawah.sawah_backend.models.ProviderReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderReviewService {

    Page<ProviderReview> getByProviderId(Long providerId, Pageable pageable);

    ProviderReview createForCompletedBooking(Long bookingId, Long touristId, ProviderReviewRequestDto requestDto);

    void deleteByTouristId(Long touristId);
}
