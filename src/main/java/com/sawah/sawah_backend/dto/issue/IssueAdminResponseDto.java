package com.sawah.sawah_backend.dto.issue;

import com.sawah.sawah_backend.enums.IssueStatus;
import com.sawah.sawah_backend.enums.ServiceRequestStatus;

import java.time.LocalDateTime;

public record IssueAdminResponseDto(
        String issueNumber,
        IssueStatus status,
        String description,
        LocalDateTime issueDate,
        LocalDateTime updatedAt,
        Long bookingId,
        ServiceRequestStatus bookingStatus,
        Long touristId,
        String touristName,
        String touristEmail,
        String touristPhoneNumber,
        Long providerId,
        String providerName,
        Long placeId,
        String placeNameAr,
        String placeNameEn,
        String placeImageUrl
) {
}
