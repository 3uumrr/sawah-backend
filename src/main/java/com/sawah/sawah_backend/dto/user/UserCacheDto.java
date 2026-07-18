package com.sawah.sawah_backend.dto.user;

import com.sawah.sawah_backend.enums.UserAccStatus;

import java.util.Set;

public record UserCacheDto(
        Long id,
        String email,
        String password,
        Set<String> roles,
        boolean isProfileComplete,
        UserAccStatus accountStatus
)
{ }
