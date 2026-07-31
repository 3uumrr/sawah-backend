package com.sawah.sawah_backend.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "password.old.required")
        String oldPassword,

        @NotBlank(message = "password.new.required")
        @Size(min = 8, max = 30, message = "user.password.size")
        String newPassword
) {}
