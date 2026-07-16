package com.sawah.sawah_backend.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewInputDto(
        @NotNull(message = "review.stars.required")
        @Min(value = 1, message = "review.stars.invalid")
        @Max(value = 5, message = "review.stars.invalid")
        Integer stars,

        @NotBlank(message = "review.content.required")
        String content
) {
}
