package com.sawah.sawah_backend.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "auth.refresh.token.required")
        String refreshToken
) {
}
