package com.sawah.sawah_backend.service.booking;

import com.sawah.sawah_backend.dto.booking.BookingRequestDto;
import com.sawah.sawah_backend.dto.booking.ProviderBookingResponseDto;
import com.sawah.sawah_backend.dto.booking.ProviderStatusRequestDto;
import com.sawah.sawah_backend.dto.booking.TouristBookingResponseDto;
import com.sawah.sawah_backend.dto.provider.ProviderEarningsStatsDto;
import com.sawah.sawah_backend.dto.providerReview.ProviderReviewPromptDto;
import com.sawah.sawah_backend.enums.ServiceRequestStatus;
import com.sawah.sawah_backend.models.ServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {

    ServiceRequest getServiceRequest(Long bookingId);
    void createBookingRequest(BookingRequestDto dto, Long currentUserId);

    Page<ProviderBookingResponseDto> getProviderBookings(Long userId, ServiceRequestStatus status, Pageable pageable);
    Page<TouristBookingResponseDto> getTouristBookings(Long userId, ServiceRequestStatus status, Pageable pageable);
    ProviderEarningsStatsDto getProviderEarningsStats(Long userId);

    void acceptBooking(Long bookingId, Long providerUserId, ProviderStatusRequestDto requestDto);
    void rejectBooking(Long bookingId, Long providerUserId, ProviderStatusRequestDto requestDto);
    void completeBookingService(Long bookingId, Long providerUserId);
    ProviderReviewPromptDto confirmCompletion(Long bookingId, Long touristId);
    void cancelBooking(Long bookingId, Long touristId);


    void rejectBookingRequest(Long bookingRequestId, Long currentUserId);


}
