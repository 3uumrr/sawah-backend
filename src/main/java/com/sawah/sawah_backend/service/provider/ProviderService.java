package com.sawah.sawah_backend.service.provider;

import com.sawah.sawah_backend.dto.provider.*;
import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.enums.ServiceCode;
import com.sawah.sawah_backend.models.Provider;
import com.sawah.sawah_backend.requests.RegisterProviderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProviderService {
    void submitProviderApplication(
            Long userId,
            RegisterProviderRequest request,
            MultipartFile nationalIdFrontImage,
            MultipartFile nationalIdBackImage);

    void completeProviderProfile(
            Long providerId,
            CompleteProviderProfileDto profileData,
            MultipartFile ProviderImage
    );

    Provider getProviderById(Long providerId);

    ProviderWithDetailsResponseDto getProviderWithDetailsById(Long providerId);
    ProviderProfileResponseDto getProviderProfileByUserId(Long userId);
    ProviderForAdminWithDetailsDto getProviderForAdminWithDetailsById(Long providerId);

    Provider getProviderByUserId(Long userId);

    ProviderForDashboardDto getProviderForDashboardByUserId(Long userId);
    Page<Provider> getProviders(ServiceCode serviceCode,
                                ProviderStatus status,
                                Boolean isAvailable,
                                Pageable pageable);

    Page<ProviderForAdminWithoutDetailsDto> getProvidersForAdmin(
            ProviderStatus status,
            ServiceCode serviceCode,
            Boolean isAvailable,
            Pageable pageable);


    // Administration/Status management
    void approveProvider(Long providerId);
    void rejectProvider(Long providerId, String rejectionReason);

    // Availability management for the provider themselves
    void updateProviderAvailability(Long providerId);

    void updateProvider(Long userId, UpdateProviderProfileRequestDto request, MultipartFile photo);

    ProviderStatus getProviderStatusByUserIdSafe(Long userId);

    Long countProviders();
    Long countProvidersByStatus(ProviderStatus providerStatus);
}
