package com.sawah.sawah_backend.dto.providerReview;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProviderReviewRequestDto(
        @NotNull(message = "provider.review.stars.required")
        @Min(value = 1, message = "provider.review.stars.invalid")
        @Max(value = 5, message = "provider.review.stars.invalid")
        Integer stars,

        String comment
) {
}
