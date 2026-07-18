package com.sawah.sawah_backend.dto.user;

public record UserResponseDto(
        String name,
        String email,
        String country,
        String phoneNumber,
        String gender,
        String profilePictureUrl
) {}
