package com.sawah.sawah_backend.dto.provider;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CompleteProviderProfileDto(

        @Size(min = 20, max = 500, message = "provider.bio.size")
        String bio,

        @NotBlank(message = "user.country.required")
        String country,

        @NotBlank(message = "user.phone.required")
        String phoneNumber,

        @NotBlank(message = "user.gender.required")
        @Pattern(regexp = "^(MALE|FEMALE)$", message = "{user.gender.invalid}")
        String gender, // Male , Female

        @NotBlank(message = "user.language.required")
        @Pattern(regexp = "^(AR|EN)$", message = "user.language.invalid")
        String preferredLanguage // Ar , En

) {
}
