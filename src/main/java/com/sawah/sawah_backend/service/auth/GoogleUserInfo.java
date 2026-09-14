package com.sawah.sawah_backend.service.auth;

public record GoogleUserInfo(
        String email,
        String subject,
        String firstName,
        String lastName
) {
}
