package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.favoritePlace.FavoritePlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/users/favorite-places")
@RequiredArgsConstructor
public class FavoritePlaceController {

    private final FavoritePlaceService favoritePlaceService;
    private final MessageSource messageSource;

    @Operation(summary = "Add favorite place", description = "Add favorite place. Required actor: TOURIST. Requires an authenticated principal; method-level role enforcement is not declared on this endpoint. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping("/{placeId}")
    public ResponseEntity<ApiResponse<Void>> addFavoritePlace(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long placeId,
            Locale locale
    ) {
        favoritePlaceService.addFavoritePlace(currentUser.getId(), placeId);

        String message = messageSource.getMessage("favorite.place.add.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Remove favorite place", description = "Remove favorite place. Required actor: TOURIST. Requires an authenticated principal; method-level role enforcement is not declared on this endpoint. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping("/{placeId}")
    public ResponseEntity<ApiResponse<Void>> removeFavoritePlace(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long placeId,
            Locale locale
    ) {
        favoritePlaceService.removeFavoritePlace(currentUser.getId(), placeId);

        String message = messageSource.getMessage("favorite.place.remove.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }
}
