package com.sawah.sawah_backend.dto.auth;

import com.sawah.sawah_backend.enums.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GoogleAuthRequestDto(
        @NotBlank(message = "google.idToken.required")
        String idToken,

        @NotNull(message = "google.accountType.required")
        RoleName accountType
) {
}
