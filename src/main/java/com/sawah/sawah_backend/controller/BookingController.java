package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.booking.BookingRequestDto;
import com.sawah.sawah_backend.dto.booking.ProviderBookingResponseDto;
import com.sawah.sawah_backend.dto.booking.ProviderStatusRequestDto;
import com.sawah.sawah_backend.dto.booking.TouristBookingResponseDto;
import com.sawah.sawah_backend.dto.providerReview.ProviderReviewPromptDto;
import com.sawah.sawah_backend.dto.providerReview.ProviderReviewRequestDto;
import com.sawah.sawah_backend.dto.providerReview.ProviderReviewResponseDto;
import com.sawah.sawah_backend.enums.ServiceRequestStatus;
import com.sawah.sawah_backend.mapper.ProviderReviewMapper;
import com.sawah.sawah_backend.models.ProviderReview;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.booking.BookingService;
import com.sawah.sawah_backend.service.providerReview.ProviderReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final ProviderReviewService providerReviewService;
    private final ProviderReviewMapper providerReviewMapper;
    private final MessageSource messageSource;

    @Operation(summary = "Create booking request", description = "Create booking request. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> createBookingRequest(
            @Valid @RequestBody BookingRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {

        bookingService.createBookingRequest(requestDto, currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Get provider bookings", description = "Get provider bookings. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProviderBookingResponseDto.class)))
    })
    @GetMapping("/provider/me")
    @PreAuthorize("hasRole('PROVIDER') and authentication.principal.providerStatus.name() == 'APPROVED'")
    public ResponseEntity<ApiResponse<Page<ProviderBookingResponseDto>>> getMyBookings(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) ServiceRequestStatus status,
            @PageableDefault(size = 20) Pageable pageable,
            Locale locale) {

        Page<ProviderBookingResponseDto> bookings = bookingService.getProviderBookings(currentUser.getId(), status, pageable);

        String message = messageSource.getMessage("common.success", null, locale);
        return ResponseEntity.ok(new ApiResponse<>(message, bookings, LocalDateTime.now()));
    }

    @Operation(summary = "Get tourist bookings", description = "Get tourist bookings. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = TouristBookingResponseDto.class)))
    })
    @GetMapping("/tourist/me")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Page<TouristBookingResponseDto>>> getMyTouristBookings(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) ServiceRequestStatus status,
            @PageableDefault(size = 20) Pageable pageable,
            Locale locale) {

        Page<TouristBookingResponseDto> bookings = bookingService.getTouristBookings(currentUser.getId(), status, pageable);

        String message = messageSource.getMessage("common.success", null, locale);
        return ResponseEntity.ok(new ApiResponse<>(message, bookings, LocalDateTime.now()));
    }

    @Operation(summary = "Accept booking", description = "Accept booking. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping("/{bookingId}/accept")
    @PreAuthorize("hasRole('PROVIDER') and authentication.principal.providerStatus.name() == 'APPROVED'")
    public ResponseEntity<ApiResponse<Void>> acceptBooking(
            @PathVariable Long bookingId,
            @RequestBody(required = false) ProviderStatusRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {

        bookingService.acceptBooking(bookingId, currentUser.getId(), requestDto);

        String message = messageSource.getMessage("booking.status.accepted_success",null,locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Reject booking", description = "Reject booking. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping("/{bookingId}/reject")
    @PreAuthorize("hasRole('PROVIDER') and authentication.principal.providerStatus.name() == 'APPROVED'")
    public ResponseEntity<ApiResponse<Void>> rejectBooking(
            @PathVariable Long bookingId,
            @RequestBody(required = false) ProviderStatusRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {

        bookingService.rejectBooking(bookingId, currentUser.getId(), requestDto);

        String message = messageSource.getMessage("booking.status.rejected_success",null,locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Mark booking service complete", description = "Mark booking service complete. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping("/{bookingId}/complete")
    @PreAuthorize("hasRole('PROVIDER') and authentication.principal.providerStatus.name() == 'APPROVED'")
    public ResponseEntity<ApiResponse<Void>> completeBookingService(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {

        bookingService.completeBookingService(bookingId, currentUser.getId());

        String message = messageSource.getMessage("booking.status.waiting_confirmation_success",null,locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Confirm booking completion", description = "Confirm booking completion. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProviderReviewPromptDto.class)))
    })
    @PutMapping("/{bookingId}/confirm-completion")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<ProviderReviewPromptDto>> confirmCompletion(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {

        ProviderReviewPromptDto reviewPrompt = bookingService.confirmCompletion(bookingId, currentUser.getId());

        String message = messageSource.getMessage("booking.status.completed_success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, reviewPrompt, LocalDateTime.now()));
    }

    @Operation(summary = "Create provider review", description = "Create provider review. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = ProviderReviewResponseDto.class)))
    })
    @PostMapping("/{bookingId}/provider-review")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<ProviderReviewResponseDto>> createProviderReview(
            @PathVariable Long bookingId,
            @Valid @RequestBody ProviderReviewRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {

        ProviderReview providerReview = providerReviewService.createForCompletedBooking(
                bookingId,
                currentUser.getId(),
                requestDto
        );
        ProviderReviewResponseDto response = providerReviewMapper.toDto(providerReview);

        String message = messageSource.getMessage("provider.review.add.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, response, LocalDateTime.now()));
    }

    @Operation(summary = "Cancel booking", description = "Cancel booking. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping("/{bookingId}/cancel")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {

        bookingService.cancelBooking(bookingId, currentUser.getId());

        String message = messageSource.getMessage("booking.status.cancelled_success", null, locale);


        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

}
