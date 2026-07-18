package com.sawah.sawah_backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @NotBlank(message = "user.name.required")
        @Size(min = 7, max = 100, message = "user.name.size")
        String name,

        @NotBlank(message = "user.email.required")
        @Email(message = "user.email.invalid")
        String email,

        @NotBlank(message = "user.country.required")
        String country,

        @NotBlank(message = "user.phone.required")
        String phoneNumber,

        @NotBlank(message = "user.gender.invalid")
        @Pattern(regexp = "^(MALE|FEMALE)$", message = "user.gender.invalid")
        String gender

){
}
