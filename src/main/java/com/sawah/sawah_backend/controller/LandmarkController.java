package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.exceptions.BadRequestException;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.service.aiService.landmark.LandmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/landmarks")
@RequiredArgsConstructor
public class LandmarkController {

    private final LandmarkService landmarkService;
    private final MessageSource messageSource;

    @Operation(summary = "Explore landmark from image", description = "Explore landmark from image. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping(
            value = "/explore",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<String>> exploreLandmark(
            @RequestPart(name = "file", required = false) MultipartFile file,
            Locale locale
    ) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("landmark.image.required");
        }

        String landmarkDetails = landmarkService.exploreLandmarkFromImage(file, locale);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, landmarkDetails, LocalDateTime.now()));
    }
}
