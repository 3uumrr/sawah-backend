package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.user.UserInterestDto;
import com.sawah.sawah_backend.mapper.CategoryMapper;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.userPreference.UserPreferencesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/users/preferences")
@RequiredArgsConstructor
public class UserPreferencesController {

    private final UserPreferencesService userPreferencesService;
    private final CategoryMapper categoryMapper;
    private final MessageSource messageSource;

    @Operation(summary = "Create user preferences", description = "Create user preferences. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> addUserPreferences(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody @NotEmpty List<Long> categoryIds,
            Locale local
    ) {

        userPreferencesService.addUserPreferences(currentUser.getId(), categoryIds);

        String message = messageSource.getMessage("user.preferences.add.success",null,local);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "List user preferences", description = "List user preferences. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserInterestDto.class)))
    })
    @GetMapping
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<List<UserInterestDto>>> getUserCategories(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale local
            ) {

        List<UserInterestDto> categories = categoryMapper
                .toListUserCategoryDto(userPreferencesService.getUserCategories(currentUser.getId()));

        String message = messageSource.getMessage("common.success",null,local);


        return ResponseEntity.ok(new ApiResponse<>(message, categories, LocalDateTime.now()));
    }

    @Operation(summary = "Delete user preference", description = "Delete user preference. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> deleteUserPreference(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long categoryId,
            Locale local
    ) {
        userPreferencesService.deleteUserPreference(currentUser.getId(), categoryId);

        String message = messageSource.getMessage("common.success", null, local);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }
}
