package com.sawah.sawah_backend.dto.provider;

import com.sawah.sawah_backend.dto.driverProfile.DriverProfileResponseDto;
import com.sawah.sawah_backend.dto.providerLanguage.ProviderLanguageResponseDto;
import com.sawah.sawah_backend.dto.user.UserResponseDto;
import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.enums.ServiceCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProviderForAdminWithDetailsDto(
        Long id,
        String bio,
        Integer experienceYears,
        BigDecimal ratePerHour,
        BigDecimal ratePerDay,
        Boolean isAvailable,
        ProviderStatus providerStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        String nationalId,
        String nationalIdFrontUrl,
        String nationalIdBackUrl,
        LocalDateTime approvedAt,
        LocalDateTime rejectedAt,
        String rejectionReason,

        BigDecimal averageRating,
        Integer totalReviews,
        Integer totalBookings,
        Integer completedBookings,

        UserResponseDto userResponseDto,
        ServiceCode serviceCode,
        List<ProviderLanguageResponseDto> languages,
        DriverProfileResponseDto driverProfile
) {
}
