package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.place.PlaceInputDto;
import com.sawah.sawah_backend.dto.place.PlaceMapMarkerDto;
import com.sawah.sawah_backend.dto.place.PlaceNearbyResponseDto;
import com.sawah.sawah_backend.dto.place.PlaceRecentSearchResponseDto;
import com.sawah.sawah_backend.dto.place.PlaceUpdateDto;
import com.sawah.sawah_backend.dto.place.PlaceWithDetailsResponseDto;
import com.sawah.sawah_backend.dto.place.PlaceWithoutDetailsResponseDto;
import com.sawah.sawah_backend.dto.review.ReviewResponseDto;
import com.sawah.sawah_backend.mapper.PlaceMapper;
import com.sawah.sawah_backend.mapper.ReviewMapper;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.place.PlaceService;
import com.sawah.sawah_backend.service.review.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final PlaceMapper placeMapper;
    private final MessageSource messageSource;

    @Operation(summary = "List all places", description = "List all places. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceWithoutDetailsResponseDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<PlaceWithoutDetailsResponseDto>>> getAllPlaces(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            Locale locale
    ) {
        Page<PlaceWithoutDetailsResponseDto> places = placeService.toWithoutDetailsPage(
                placeService.getAllPlaces(pageable), currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, places, LocalDateTime.now()));
    }

    @Operation(summary = "List places by category", description = "List places by category. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceWithoutDetailsResponseDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Page<PlaceWithoutDetailsResponseDto>>> getPlacesByCategory(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long categoryId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            Locale locale
    ) {
        Page<PlaceWithoutDetailsResponseDto> places = placeService.toWithoutDetailsPage(
                placeService.findByCategoryId(categoryId, pageable), currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, places, LocalDateTime.now()));
    }

    @Operation(summary = "List places by governorate", description = "List places by governorate. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceWithoutDetailsResponseDto.class)))
    })
    @GetMapping("/governorate")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Page<PlaceWithoutDetailsResponseDto>>> getPlacesByGovernorate(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam String name,
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            Locale locale
    ) {
        Page<PlaceWithoutDetailsResponseDto> places = placeService.toWithoutDetailsPage(
                placeService.findByGovernorateArContainingIgnoreCaseOrGovernorateEnContainingIgnoreCase(name, name, pageable),
                currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, places, LocalDateTime.now()));
    }

    @Operation(summary = "List favorite places", description = "List favorite places. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceWithoutDetailsResponseDto.class)))
    })
    @GetMapping("/favorites")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Page<PlaceWithoutDetailsResponseDto>>> getFavoritePlaces(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PageableDefault(size = 20) Pageable pageable,
            Locale locale
    ) {
        Page<PlaceWithoutDetailsResponseDto> places = placeService.toWithoutDetailsPage(
                placeService.findFavoritePlacesByUserId(currentUser.getId(), pageable), currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, places, LocalDateTime.now()));
    }

    @Operation(summary = "List visited places", description = "List visited places. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceWithoutDetailsResponseDto.class)))
    })
    @GetMapping("/visited")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Page<PlaceWithoutDetailsResponseDto>>> getVisitedPlaces(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PageableDefault(size = 20) Pageable pageable,
            Locale locale
    ) {
        Page<PlaceWithoutDetailsResponseDto> places = placeService.toWithoutDetailsPage(
                placeService.findVisitedPlacesByUserId(currentUser.getId(), pageable), currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, places, LocalDateTime.now()));
    }

    @Operation(summary = "List popular places", description = "List popular places. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceWithoutDetailsResponseDto.class)))
    })
    @GetMapping("/popular")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Page<PlaceWithoutDetailsResponseDto>>> getPopularPlaces(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PageableDefault(size = 20) Pageable pageable,
            Locale locale
    ) {
        Page<PlaceWithoutDetailsResponseDto> places = placeService.toWithoutDetailsPage(
                placeService.findAllByOrderByRatingDesc(pageable), currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, places, LocalDateTime.now()));
    }

    @Operation(summary = "List recent place searches", description = "List recent place searches. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceRecentSearchResponseDto.class)))
    })
    @GetMapping("/recent-searches")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Page<PlaceRecentSearchResponseDto>>> getRecentSearches(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale
    ) {
        Page<PlaceRecentSearchResponseDto> places = placeService.getRecentSearches(currentUser.getId())
                .map(placeMapper::toRecentSearchResponseDto);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, places, LocalDateTime.now()));
    }

    @Operation(summary = "Get place details", description = "Get place details. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceWithDetailsResponseDto.class)))
    })
    @GetMapping("/{placeId}")
    public ResponseEntity<ApiResponse<PlaceWithDetailsResponseDto>> getPlaceWithDetails(
            @PathVariable Long placeId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale
    ) {
        PlaceWithDetailsResponseDto place = placeService.getPlaceWithDetailsById(placeId, currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, place, LocalDateTime.now()));
    }

    @Operation(summary = "Create place", description = "Create place. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> addPlace(
            @RequestPart(name = "place") @Valid PlaceInputDto placeInputDto,
            @RequestPart(name = "images") List<MultipartFile> placeImages,
            Locale locale
    ) {
        placeService.addPlace(placeInputDto, placeImages);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Update place", description = "Update place. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updatePlace(
            @PathVariable Long id,
            @RequestPart(name = "place") @Valid PlaceUpdateDto placeUpdateDto,
            @RequestPart(name = "images", required = false) List<MultipartFile> placeImages,
            Locale locale
    ) {
        placeService.updatePlace(id, placeUpdateDto, placeImages);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Delete place", description = "Delete place. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping("/{id}")
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePlace(
            @PathVariable Long id,
            Locale locale
    ) {
        placeService.deletePlaceById(id);

        String message = messageSource.getMessage("place.delete.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Get place details", description = "Get place details. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceWithoutDetailsResponseDto.class)))
    })
    @GetMapping("/typeahead")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Page<PlaceWithoutDetailsResponseDto>>> getPlaceWithDetails(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam("q") String query,
            Locale locale
    ) {
        Page<PlaceWithoutDetailsResponseDto> places = placeService.toWithoutDetailsPage(
                placeService.findSuggestions(query), currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, places, LocalDateTime.now()));
    }

    @Operation(summary = "List nearby places", description = "List nearby places. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceNearbyResponseDto.class)))
    })
    @GetMapping("/nearby")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<List<com.sawah.sawah_backend.dto.place.PlaceNearbyResponseDto>>> getNearbyPlaces(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam java.math.BigDecimal latitude,
            @RequestParam java.math.BigDecimal longitude,
            Locale locale
    ) {
        List<com.sawah.sawah_backend.dto.place.PlaceNearbyResponseDto> places = placeService.toNearbyResponseList(
                placeService.findTop5NearbyByLocation(latitude, longitude), 
                currentUser.getId()
        );

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, places, LocalDateTime.now()));
    }

    @Operation(summary = "List places within map bounds", description = "List places within map bounds. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceMapMarkerDto.class)))
    })
    @PostMapping("/within-bounds")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<List<com.sawah.sawah_backend.dto.place.PlaceMapMarkerDto>>> getPlacesWithinBounds(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @org.springframework.web.bind.annotation.RequestBody @Valid com.sawah.sawah_backend.dto.place.MapBoundsDto bounds,
            Locale locale
    ) {
        List<com.sawah.sawah_backend.dto.place.PlaceMapMarkerDto> places = placeService.getPlacesWithinBounds(bounds);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, places, LocalDateTime.now()));
    }

    @Operation(summary = "List recommended places", description = "List recommended places. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PlaceWithoutDetailsResponseDto.class)))
    })
    @GetMapping("/recommendations")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<List<PlaceWithoutDetailsResponseDto>>> getRecommendedPlaces(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale
    ) {
        List<PlaceWithoutDetailsResponseDto> places = placeService.getRecommendedPlaces(currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, places, LocalDateTime.now()));
    }

}
