package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.provider.CompleteProviderProfileDto;
import com.sawah.sawah_backend.dto.provider.ProviderForDashboardDto;
import com.sawah.sawah_backend.dto.provider.ProviderForAdminWithoutDetailsDto;
import com.sawah.sawah_backend.dto.provider.UpdateProviderProfileRequestDto;
import com.sawah.sawah_backend.dto.provider.ProviderEarningsStatsDto;
import com.sawah.sawah_backend.dto.provider.ProviderForAdminWithDetailsDto;
import com.sawah.sawah_backend.dto.provider.ProviderProfileResponseDto;
import com.sawah.sawah_backend.dto.provider.ProviderWithDetailsResponseDto;
import com.sawah.sawah_backend.dto.provider.ProviderWithoutDetailsResponseDto;
import com.sawah.sawah_backend.dto.providerReview.ProviderReviewResponseDto;
import com.sawah.sawah_backend.enums.ProviderStatus;
import com.sawah.sawah_backend.enums.ServiceCode;
import com.sawah.sawah_backend.enums.ServiceRequestStatus;
import com.sawah.sawah_backend.mapper.ProviderMapper;
import com.sawah.sawah_backend.mapper.ProviderReviewMapper;
import com.sawah.sawah_backend.models.ProviderReview;
import com.sawah.sawah_backend.requests.RegisterProviderRequest;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.provider.ProviderService;
import com.sawah.sawah_backend.service.providerReview.ProviderReviewService;
import com.sawah.sawah_backend.service.booking.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.Locale;

import com.sawah.sawah_backend.models.Provider;

@RestController
@RequestMapping("${api.prefix}/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;
    private final ProviderReviewService providerReviewService;
    private final BookingService bookingService;
    private final MessageSource messageSource;
    private final ProviderMapper  providerMapper;
    private final ProviderReviewMapper providerReviewMapper;

    @Operation(summary = "Submit provider application", description = "Submit provider application. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<Void>> registerProvider(
            @RequestPart(name = "provider") @Valid RegisterProviderRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestPart(name = "nationalIdFrontImage") MultipartFile nationalIdFrontImage,
            @RequestPart(name = "nationalIdBackImage") MultipartFile nationalIdBackImage,
            Locale locale) {
        providerService.submitProviderApplication(
                currentUser.getId(),
                request,
                nationalIdFrontImage,
                nationalIdBackImage);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Complete provider profile", description = "Complete provider profile. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping(value = "/complete-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<Void>> completeProviderProfile(
            @RequestPart(name = "provider") @Valid CompleteProviderProfileDto profileData,
            @RequestPart(required = false, name = "image") MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {
        providerService.completeProviderProfile(currentUser.getId(), profileData, image);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Update provider profile", description = "Update provider profile. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER') and authentication.principal.providerStatus == 'APPROVED'. Approved provider status is also required. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PROVIDER') and authentication.principal.providerStatus.name() == 'APPROVED'")
    public ResponseEntity<ApiResponse<Void>> updateProviderProfile(
            @RequestPart(name = "provider") @Valid UpdateProviderProfileRequestDto request,
            @RequestPart(required = false, name = "photo") MultipartFile photo,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {
        providerService.updateProvider(currentUser.getId(), request, photo);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Approve provider", description = "Approve provider. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PatchMapping("/{providerId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin")
    public ResponseEntity<ApiResponse<Void>> approveProvider(
            @PathVariable Long providerId,
            Locale locale) {
        providerService.approveProvider(providerId);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Reject provider", description = "Reject provider. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PatchMapping("/{providerId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin")
    public ResponseEntity<ApiResponse<Void>> rejectProvider(
            @PathVariable Long providerId,
            @RequestParam String rejectionReason,
            Locale locale) {
        providerService.rejectProvider(providerId, rejectionReason);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Toggle provider availability", description = "Toggle provider availability. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER') and authentication.principal.providerStatus == 'APPROVED'. Approved provider status is also required. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PatchMapping("/me/availability")
    @PreAuthorize("hasRole('PROVIDER') and authentication.principal.providerStatus.name() == 'APPROVED'")
    public ResponseEntity<ApiResponse<Void>> updateProviderAvailability(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {
        providerService.updateProviderAvailability(currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }


    @Operation(summary = "List providers for admin", description = "List providers for admin. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProviderForAdminWithoutDetailsDto.class)))
    })
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin")
    public ResponseEntity<ApiResponse<Page<ProviderForAdminWithoutDetailsDto>>> getProvidersForAdmin(
            @RequestParam(required = false) ProviderStatus status,
            @RequestParam(required = false) ServiceCode serviceCode,
            @RequestParam(required = false) Boolean isAvailable,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Locale locale) {
        Page<ProviderForAdminWithoutDetailsDto> providers = providerService.getProvidersForAdmin(
                status,
                serviceCode,
                isAvailable,
                pageable
        );

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, providers, LocalDateTime.now()));
    }

    @Operation(summary = "Get provider details for admin", description = "Get provider details for admin. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProviderForAdminWithDetailsDto.class)))
    })
    @GetMapping("/admin/{providerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin")
    public ResponseEntity<ApiResponse<ProviderForAdminWithDetailsDto>> getProviderForAdminWithDetails(
            @PathVariable Long providerId,
            Locale locale) {
        ProviderForAdminWithDetailsDto provider = providerService.getProviderForAdminWithDetailsById(providerId);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, provider, LocalDateTime.now()));
    }

    @Operation(summary = "Get provider dashboard overview", description = "Get provider dashboard overview. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER') and authentication.principal.providerStatus == 'APPROVED'. Approved provider status is also required. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProviderForDashboardDto.class)))
    })
    @GetMapping("/dashboard/overview")
    @PreAuthorize("hasRole('PROVIDER') and authentication.principal.providerStatus.name() == 'APPROVED'")
    public ResponseEntity<ApiResponse<ProviderForDashboardDto>> getProviderDashboardOverview(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {
        ProviderForDashboardDto provider = providerService.getProviderForDashboardByUserId(currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, provider, LocalDateTime.now()));
    }

    @Operation(summary = "Get my provider profile", description = "Get my provider profile. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProviderProfileResponseDto.class)))
    })
    @GetMapping("/me")
    @PreAuthorize("hasRole('PROVIDER') and authentication.principal.providerStatus.name() == 'APPROVED'")
    public ResponseEntity<ApiResponse<ProviderProfileResponseDto>> getMyProviderDetails(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {
        ProviderProfileResponseDto providerDto = providerService.getProviderProfileByUserId(currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, providerDto, LocalDateTime.now()));
    }

    @Operation(summary = "List my provider reviews", description = "List my provider reviews. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProviderReviewResponseDto.class)))
    })
    @GetMapping("/me/reviews")
    @PreAuthorize("hasRole('PROVIDER') and authentication.principal.providerStatus.name() == 'APPROVED'")
    public ResponseEntity<ApiResponse<Page<ProviderReviewResponseDto>>> getMyProviderReviews(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable,
            Locale locale) {
        Provider provider = providerService.getProviderByUserId(currentUser.getId());
        Page<ProviderReview> providerReviews = providerReviewService.getByProviderId(provider.getId(), pageable);
        Page<ProviderReviewResponseDto> response = providerReviews.map(providerReviewMapper::toDto);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, response, LocalDateTime.now()));
    }

    @Operation(summary = "Get my earnings stats", description = "Get my earnings stats. Required actor: PROVIDER. Security constraint: hasRole('PROVIDER'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProviderEarningsStatsDto.class)))
    })
    @GetMapping("/me/earnings-stats")
    @PreAuthorize("hasRole('PROVIDER') and authentication.principal.providerStatus.name() == 'APPROVED'")
    public ResponseEntity<ApiResponse<ProviderEarningsStatsDto>> getMyEarningsStats(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {

        ProviderEarningsStatsDto statsDto = bookingService.getProviderEarningsStats(currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, statsDto, LocalDateTime.now()));
    }

    @Operation(summary = "Get provider details", description = "Get provider details. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProviderWithDetailsResponseDto.class)))
    })
    @GetMapping("/{providerId}")
    public ResponseEntity<ApiResponse<ProviderWithDetailsResponseDto>> getProviderWithDetails(
            @PathVariable Long providerId,
            Locale locale) {
        ProviderWithDetailsResponseDto provider = providerService.getProviderWithDetailsById(providerId);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, provider, LocalDateTime.now()));
    }

    @Operation(summary = "List provider reviews", description = "List provider reviews. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProviderReviewResponseDto.class)))
    })
    @GetMapping("/{providerId}/reviews")
    public ResponseEntity<ApiResponse<Page<ProviderReviewResponseDto>>> getProviderReviews(
            @PathVariable Long providerId,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable,
            Locale locale) {
        Page<ProviderReview> providerReviews = providerReviewService.getByProviderId(providerId, pageable);
        Page<ProviderReviewResponseDto> response = providerReviews.map(providerReviewMapper::toDto);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, response, LocalDateTime.now()));
    }

    @Operation(summary = "List providers for tourists", description = "List providers for tourists. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ProviderWithoutDetailsResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProviderWithoutDetailsResponseDto>>> getProvidersForTourists(
            @RequestParam(required = true) ServiceCode serviceCode,
            @RequestParam(required = false) String orderBy,
            @PageableDefault(size = 20) Pageable pageable,
            Locale locale) {

        Sort sort = Sort.by("id").ascending();
        if ("rating".equalsIgnoreCase(orderBy)) {
            sort = Sort.by("averageRating").descending();
        } else if ("pricePerHour".equalsIgnoreCase(orderBy)) {
            sort = Sort.by("ratePerHour").ascending();
        } else if ("pricePerDay".equalsIgnoreCase(orderBy)) {
            sort = Sort.by("ratePerDay").ascending();
        }

        Pageable finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Provider> providers = providerService.getProviders(
                serviceCode,
                ProviderStatus.APPROVED,
                true,
                finalPageable
        );

        Page<ProviderWithoutDetailsResponseDto> providersDto = providers.map(providerMapper::providerWithoutDetailsResponseDto);

        String message = messageSource.getMessage("common.success", null, locale);
        return ResponseEntity.ok(new ApiResponse<>(message, providersDto, LocalDateTime.now()));
    }
}
