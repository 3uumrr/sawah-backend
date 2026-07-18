package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.recentSearch.RecentSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/users/recent-searches")
@RequiredArgsConstructor
public class RecentSearchController {

    private final RecentSearchService recentSearchService;
    private final MessageSource messageSource;

    @Operation(summary = "Add recent search", description = "Add recent search. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping("/{placeId}")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> addRecentSearch(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long placeId,
            Locale locale
    ) {
        recentSearchService.addRecentSearch(currentUser.getId(), placeId);

        String message = messageSource.getMessage("recent.search.add.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Clear recent searches", description = "Clear recent searches. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> clearRecentSearches(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale
    ) {
        recentSearchService.clearRecentSearches(currentUser.getId());

        String message = messageSource.getMessage("recent.search.clear.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Delete recent search", description = "Delete recent search. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> deleteRecentSearchById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale
    ) {
        recentSearchService.deleteRecentSearchById(id, currentUser.getId());

        String message = messageSource.getMessage("recent.search.delete.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }
}
