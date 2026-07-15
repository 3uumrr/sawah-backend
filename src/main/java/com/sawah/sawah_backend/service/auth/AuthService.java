package com.sawah.sawah_backend.service.auth;

import com.sawah.sawah_backend.dto.auth.ResetPasswordRequest;
import com.sawah.sawah_backend.dto.auth.GoogleAuthRequestDto;
import com.sawah.sawah_backend.dto.user.UserInputDto;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.requests.LoginRequest;
import com.sawah.sawah_backend.response.AuthResponse;

import java.util.Locale;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse loginWithGoogle(GoogleAuthRequestDto request);
    void signUp(UserInputDto user, String accountType);

    void initiatePasswordReset(String email, Locale locale);
    void resetPassword(ResetPasswordRequest request) throws ResourceNotFoundException;
}
