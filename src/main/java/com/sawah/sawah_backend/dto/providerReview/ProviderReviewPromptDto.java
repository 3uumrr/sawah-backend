package com.sawah.sawah_backend.dto.providerReview;

public record ProviderReviewPromptDto(
        Long bookingId,
        Long providerId,
        String providerFullName,
        String providerImageUrl,
        String providerRoleLabel,
        String placeNameEn,
        String placeNameAr
) {
}
