package com.sawah.sawah_backend.dto.provider;

import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.enums.ServiceCode;

import java.time.LocalDateTime;

public record ProviderForAdminWithoutDetailsDto(
        Long id,
        String fullName,
        String email,
        String profilePictureUrl,
        Boolean isAvailable,
        ProviderStatus accountStatus,
        ServiceCode serviceCode,
        LocalDateTime createdAt
) {
}
