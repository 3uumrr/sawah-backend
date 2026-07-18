package com.sawah.sawah_backend.dto.user;

import java.time.LocalDateTime;
import java.util.Set;

public record UserAdminResponseDto (
    Long id,
    String name,
    String email,
    String country,
    String phoneNumber,
    String gender,
    String preferredLanguage,
    String profilePictureUrl,
    String accountStatus,
    Set<String> roles,
    LocalDateTime createdAt,
    LocalDateTime updatedAt)
    {}
