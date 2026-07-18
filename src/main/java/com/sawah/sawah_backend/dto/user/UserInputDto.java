package com.sawah.sawah_backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserInputDto(
        @NotBlank(message = "user.firstname.required")
        @Size(min = 3, max = 50, message = "user.firstname.size")
        String firstName,

        @NotBlank(message = "user.lastname.required")
        @Size(min = 3, max = 50, message = "user.lastname.size")
        String lastName,

        @NotBlank(message = "user.email.require")
        @Email(message = "user.email.invalid")
        String email,

        @NotBlank(message = "user.password.required")
        @Size(min = 8, max = 30, message = "user.password.size")
        String password
)
{}
