package com.sawah.sawah_backend.service.booking;

import com.sawah.sawah_backend.dto.booking.BookingRequestDto;
import com.sawah.sawah_backend.dto.booking.ProviderStatusRequestDto;
import com.sawah.sawah_backend.dto.booking.TouristBookingResponseDto;
import com.sawah.sawah_backend.dto.providerReview.ProviderReviewPromptDto;
import com.sawah.sawah_backend.enums.ServiceRequestStatus;
import com.sawah.sawah_backend.exceptions.BadRequestException;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.mapper.BookingMapper;
import com.sawah.sawah_backend.models.Place;
import com.sawah.sawah_backend.models.Provider;
import com.sawah.sawah_backend.models.ServiceRequest;
import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.repository.ServiceRequestRepository;
import com.sawah.sawah_backend.service.notification.NotificationService;
import com.sawah.sawah_backend.service.place.PlaceService;
import com.sawah.sawah_backend.service.provider.ProviderService;
import com.sawah.sawah_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.sawah.sawah_backend.dto.booking.ProviderBookingResponseDto;
import com.sawah.sawah_backend.dto.provider.ProviderEarningsStatsDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final UserService userService;
    private final PlaceService placeService;
    private final ProviderService providerService;
    private final BookingMapper bookingMapper;
    private final NotificationService notificationService;

    @Override
    public ServiceRequest getServiceRequest(Long bookingId) {
        return serviceRequestRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("booking.not.found"));
    }

    @Override
    @Transactional
    public void createBookingRequest(BookingRequestDto dto, Long currentUserId) {

        User tourist = userService.getUserById(currentUserId);

        Place place = placeService.getPlaceById(dto.placeId());

        Provider provider = providerService.getProviderById(dto.providerId());

        ServiceRequest serviceRequest = bookingMapper.toEntity(dto);
        
        serviceRequest.setTourist(tourist);
        serviceRequest.setPlace(place);
        serviceRequest.setProvider(provider);
        
        if (dto.pickupLatitude() != null) {
            serviceRequest.setPickupLatitude(dto.pickupLatitude());
        }
        if (dto.pickupLongitude() != null) {
            serviceRequest.setPickupLongitude(dto.pickupLongitude());
        }
        
        if (dto.durationDays() != null && provider.getRatePerDay() != null) {
            BigDecimal totalPrice = provider.getRatePerDay().multiply(BigDecimal.valueOf(dto.durationDays()));
            serviceRequest.setTotalPrice(totalPrice);
            serviceRequest.setDurationDays(dto.durationDays());
        } else if (dto.durationHours() != null && provider.getRatePerHour() != null) {
            BigDecimal totalPrice = provider.getRatePerHour().multiply(BigDecimal.valueOf(dto.durationHours()));
            serviceRequest.setTotalPrice(totalPrice);
        } else {
            serviceRequest.setTotalPrice(BigDecimal.ZERO);
        }
        
        // Ensure the service is set since it's non-nullable in ServiceRequest
        if (provider.getService() != null) {
            serviceRequest.setService(provider.getService());
        }


        serviceRequest.setStatus(ServiceRequestStatus.PENDING);

        serviceRequestRepository.save(serviceRequest);
        
        notificationService.createAndSendNotification(
                provider.getUser().getId(),
                "notification.booking.new.title",
                "notification.booking.new.body",
                List.of((tourist.getFirstName() + " " + tourist.getLastName()).trim()),
                serviceRequest.getId()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderBookingResponseDto> getProviderBookings(Long userId, ServiceRequestStatus status, Pageable pageable) {

        Provider provider = providerService.getProviderByUserId(userId);

        if (status != null) {
            return serviceRequestRepository.findByProviderIdAndStatus(provider.getId(), status, pageable)
                    .map(bookingMapper::toProviderBookingResponseDto);
        }

        return serviceRequestRepository.findByProviderId(provider.getId(), pageable)
                .map(bookingMapper::toProviderBookingResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TouristBookingResponseDto> getTouristBookings(Long userId, ServiceRequestStatus status, Pageable pageable) {

        if (status != null) {
            return serviceRequestRepository.findByTouristIdAndStatus(userId, status, pageable)
                    .map(bookingMapper::toTouristBookingResponseDto);
        }

        return serviceRequestRepository.findByTouristId(userId, pageable)
                .map(bookingMapper::toTouristBookingResponseDto);
    }



    @Override
    @Transactional(readOnly = true)
    public ProviderEarningsStatsDto getProviderEarningsStats(Long userId) {
        Provider provider = providerService.getProviderByUserId(userId);
        Long providerId = provider.getId();

        Double totalEarnings = serviceRequestRepository.sumTotalEarnings(providerId);
        Double weeklyEarnings = serviceRequestRepository.sumEarningsSince(providerId, LocalDateTime.now().minusWeeks(1));
        Double monthlyEarnings = serviceRequestRepository.sumEarningsSince(providerId, LocalDateTime.now().minusMonths(1));
        Long completedToursCount = serviceRequestRepository.countByProviderAndStatus(providerId, ServiceRequestStatus.COMPLETED);
        Long cancelledToursCount = serviceRequestRepository.countByProviderAndStatus(providerId, ServiceRequestStatus.CANCELLED);

        return new ProviderEarningsStatsDto(
                totalEarnings,
                weeklyEarnings,
                monthlyEarnings,
                completedToursCount,
                cancelledToursCount
        );
    }

    @Override
    @Transactional
    public void acceptBooking(Long bookingId, Long providerUserId, ProviderStatusRequestDto requestDto) {
        ServiceRequest booking = getServiceRequest(bookingId);
        Provider provider = providerService.getProviderByUserId(providerUserId);

        validateProviderOwnership(booking, provider.getId());
        validateNotTerminalStatus(booking);
        validateCurrentStatus(booking, ServiceRequestStatus.PENDING);

        applyProviderResponseMessage(booking, requestDto);
        booking.setStatus(ServiceRequestStatus.ACCEPTED);
        booking.setAcceptedAt(LocalDateTime.now());

        notificationService.createAndSendNotification(
                booking.getTourist().getId(),
                "notification.booking.accepted.title",
                "notification.booking.accepted.body",
                List.of(resolveProviderName(provider)),
                booking.getId()
        );
    }

    @Override
    @Transactional
    public void rejectBooking(Long bookingId, Long providerUserId, ProviderStatusRequestDto requestDto) {
        ServiceRequest booking = getServiceRequest(bookingId);
        Provider provider = providerService.getProviderByUserId(providerUserId);

        validateProviderOwnership(booking, provider.getId());
        validateNotTerminalStatus(booking);
        validateCurrentStatus(booking, ServiceRequestStatus.PENDING);

        applyProviderResponseMessage(booking, requestDto);
        booking.setStatus(ServiceRequestStatus.REJECTED);
        booking.setRejectedAt(LocalDateTime.now());

        notificationService.createAndSendNotification(
                booking.getTourist().getId(),
                "notification.booking.rejected.title",
                "notification.booking.rejected.body",
                List.of(resolveProviderName(provider)),
                booking.getId()
        );
    }

    @Override
    @Transactional
    public void completeBookingService(Long bookingId, Long providerUserId) {
        ServiceRequest booking = getServiceRequest(bookingId);
        Provider provider = providerService.getProviderByUserId(providerUserId);

        validateProviderOwnership(booking, provider.getId());
        validateNotTerminalStatus(booking);
        validateCurrentStatus(booking, ServiceRequestStatus.ACCEPTED);

        booking.setStatus(ServiceRequestStatus.WAITING_FOR_CONFIRMATION);

        notificationService.createAndSendNotification(
                booking.getTourist().getId(),
                "notification.tour_completion.title",
                "notification.tour_completion.body",
                List.of(resolveProviderName(provider), resolveTourName(booking)),
                booking.getId()
        );
    }

    @Override
    @Transactional
    public ProviderReviewPromptDto confirmCompletion(Long bookingId, Long touristId) {
        ServiceRequest booking = getServiceRequest(bookingId);
        validateTouristOwnership(booking, touristId);
        validateNotTerminalStatus(booking);
        validateCurrentStatus(booking, ServiceRequestStatus.WAITING_FOR_CONFIRMATION);

        booking.setStatus(ServiceRequestStatus.COMPLETED);
        booking.setCompletedAt(LocalDateTime.now());
        booking.getProvider().setTotalBookings(booking.getProvider().getTotalBookings() + 1);

        User tourist = booking.getTourist();
        notificationService.createAndSendNotification(
                booking.getProvider().getUser().getId(),
                "notification.booking.completed.title",
                "notification.booking.completed.body",
                List.of((tourist.getFirstName() + " " + tourist.getLastName()).trim()),
                booking.getId()
        );

        return toProviderReviewPromptDto(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId, Long touristId) {
        ServiceRequest booking = getServiceRequest(bookingId);

        validateTouristOwnership(booking, touristId);


        booking.setStatus(ServiceRequestStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());

        User tourist = booking.getTourist();
        notificationService.createAndSendNotification(
                booking.getProvider().getUser().getId(),
                "notification.booking.cancelled.title",
                "notification.booking.cancelled.body",
                List.of((tourist.getFirstName() + " " + tourist.getLastName()).trim()),
                booking.getId()
        );
    }


    @Override
    @Transactional
    public void rejectBookingRequest(Long bookingRequestId, Long currentUserId) {
        rejectBooking(bookingRequestId, currentUserId, null);
    }

    private void applyProviderResponseMessage(ServiceRequest booking, ProviderStatusRequestDto requestDto) {
        if (requestDto == null || requestDto.providerResponseMessage() == null) {
            return;
        }

        String providerResponseMessage = requestDto.providerResponseMessage().trim();
        if (!providerResponseMessage.isEmpty()) {
            booking.setProviderResponseMessage(providerResponseMessage);
        }
    }

    private void validateProviderOwnership(ServiceRequest booking, Long providerId) {
        if (!booking.getProvider().getId().equals(providerId)) {
            throw new BadRequestException("booking.unauthorized.access");
        }
    }

    private void validateTouristOwnership(ServiceRequest booking, Long touristId) {
        if (!booking.getTourist().getId().equals(touristId)) {
            throw new BadRequestException("booking.unauthorized.access");
        }
    }

    private void validateCurrentStatus(ServiceRequest booking, ServiceRequestStatus expectedStatus) {
        if (booking.getStatus() != expectedStatus) {
            throw new BadRequestException("booking.status.invalid_transition");
        }
    }

    private void validateNotTerminalStatus(ServiceRequest booking) {
        Set<ServiceRequestStatus> terminalStatuses = Set.of(
                ServiceRequestStatus.COMPLETED,
                ServiceRequestStatus.CANCELLED,
                ServiceRequestStatus.REJECTED
        );

        if (terminalStatuses.contains(booking.getStatus())) {
            throw new BadRequestException("booking.status.invalid_transition");
        }
    }

    private String resolveProviderName(Provider provider) {
        User user = provider.getUser();
        return (user.getFirstName() + " " + user.getLastName()).trim();
    }

    private String resolveTourName(ServiceRequest booking) {
        if (booking.getPlace() != null && booking.getPlace().getNameEn() != null) {
            return booking.getPlace().getNameEn();
        }

        return booking.getService().getNameEn();
    }

    private ProviderReviewPromptDto toProviderReviewPromptDto(ServiceRequest booking) {
        Provider provider = booking.getProvider();

        return new ProviderReviewPromptDto(
                booking.getId(),
                provider.getId(),
                resolveProviderName(provider),
                resolveProviderImageUrl(provider),
                booking.getService().getNameEn(),
                booking.getPlace().getNameEn(),
                booking.getPlace().getNameAr()
        );
    }

    private String resolveProviderImageUrl(Provider provider) {
        User providerUser = provider.getUser();
        if (providerUser == null || providerUser.getProfilePictureUrl() == null) {
            return null;
        }

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user_photos/")
                .path(providerUser.getProfilePictureUrl())
                .toUriString();
    }
}
