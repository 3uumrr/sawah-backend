package com.sawah.sawah_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "user.email.required")
        @Email(message = "user.email.invalid")
        String email,

        @NotBlank(message = "otp.required")
        @Size(min = 6, max = 6, message = "otp.invalid.size")
        String otp,

        @NotBlank(message = "user.password.required")
        @Size(min = 8, max = 30, message = "user.password.size")
        String newPassword)
{}
