package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.visitedPlace.VisitedPlaceService;
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
@RequestMapping("${api.prefix}/users/visited-places")
@RequiredArgsConstructor
public class VisitedPlaceController {

    private final VisitedPlaceService visitedPlaceService;
    private final MessageSource messageSource;

    @PostMapping("/{placeId}")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> addVisitedPlace(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long placeId,
            Locale locale
    ) {
        visitedPlaceService.addVisitedPlace(currentUser.getId(), placeId);

        String message = messageSource.getMessage("visited.place.add.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @DeleteMapping("/{placeId}")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> removeVisitedPlace(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long placeId,
            Locale locale
    ) {
        visitedPlaceService.removeVisitedPlace(currentUser.getId(), placeId);

        String message = messageSource.getMessage("visited.place.remove.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }
}
