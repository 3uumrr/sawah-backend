package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.auth.GoogleAuthRequestDto;
import com.sawah.sawah_backend.dto.auth.ResetPasswordRequest;
import com.sawah.sawah_backend.dto.user.UserInputDto;
import com.sawah.sawah_backend.requests.LoginRequest;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.response.AuthResponse;
import com.sawah.sawah_backend.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    private final AuthService authService;
    private final MessageSource messageSource;


    @Operation(summary = "Authenticate user and issue tokens", description = "Authenticate user and issue tokens. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request , Locale locale){

        AuthResponse authResponse = authService.login(request);

        String message = messageSource.getMessage("auth.login.success",null,locale);

        authResponse.setMessage(message);
        authResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity
                .ok(authResponse);

    }

    @Operation(summary = "Authenticate with Google", description = "Authenticate with a Google ID token and issue Sawah tokens. Public endpoint; authentication is not enforced by method-level security.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    })
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> loginWithGoogle(
            @RequestBody @Valid GoogleAuthRequestDto request,
            Locale locale) {

        AuthResponse authResponse = authService.loginWithGoogle(request);

        String message = messageSource.getMessage("auth.login.success", null, locale);

        authResponse.setMessage(message);
        authResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Register a tourist account", description = "Register a tourist account. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<Void>> signUp(
            @RequestBody @Valid UserInputDto user,
            Locale locale
    ){

        authService.signUp(user,"TOURIST");

        String message = messageSource.getMessage("user.add.success",null,locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Register a provider account", description = "Register a provider account. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping("/provider/sign-up")
    public ResponseEntity<ApiResponse<Void>> signUpAsProvider(
            @RequestBody @Valid UserInputDto user,
            Locale locale
    ){

        authService.signUp(user,"PROVIDER");

        String message = messageSource.getMessage("user.add.success",null,locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }



    @Operation(summary = "Start password reset", description = "Start password reset. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> initiatePasswordReset(@RequestBody Map<String,String> request, Locale locale) {
        String email = request.get("email");
        authService.initiatePasswordReset(email, locale);

        String message = messageSource.getMessage("password.reset.email.sent",null,locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }


    @Operation(summary = "Reset password", description = "Reset password. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request, Locale locale) {

        authService.resetPassword(request);

        String message = messageSource.getMessage("password.reset.success",null,locale);


        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }


}

