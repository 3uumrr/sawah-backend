package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.review.ReviewInputDto;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final MessageSource messageSource;

    @Operation(summary = "Create place review", description = "Create place review. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Void.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PostMapping("/places/{placeId}")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> addReview(
            @PathVariable Long placeId,
            @RequestBody @Valid ReviewInputDto reviewInputDto,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale
    ) {
        reviewService.create(reviewInputDto, placeId, currentUser.getId());

        String message = messageSource.getMessage("review.add.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Update place review", description = "Update place review. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> updateReview(
            @PathVariable Long id,
            @RequestBody @Valid ReviewInputDto reviewInputDto,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale
    ) {
        reviewService.update(id, reviewInputDto.stars(), reviewInputDto.content(), currentUser.getId());

        String message = messageSource.getMessage("review.update.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Delete place review", description = "Delete place review. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale
    ) {
        reviewService.delete(id, currentUser.getId());

        String message = messageSource.getMessage("review.delete.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }
}
