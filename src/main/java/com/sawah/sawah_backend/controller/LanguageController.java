package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.language.LanguageInputDto;
import com.sawah.sawah_backend.models.Language;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.service.language.LanguageService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;
    private final MessageSource messageSource;


    @Operation(summary = "List languages", description = "List languages. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Language.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<Language>>> getAll(Locale locale) {

        List<Language> languages = languageService.getLanguages();

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, languages, LocalDateTime.now()));
    }

    @Operation(summary = "Get language by ID", description = "Get language by ID. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Language.class)))
    })
    @GetMapping("/{id}")
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Language>> getById(@PathVariable Long id, Locale locale) {

        Language language = languageService.getById(id);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, language, LocalDateTime.now()));
    }


    @Operation(summary = "Create language", description = "Create language. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> addLanguage(
            @RequestBody @Valid LanguageInputDto languageInputDto,
            Locale locale) {

        languageService.addLanguage(languageInputDto);

        String message = messageSource.getMessage("language.add.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Delete language", description = "Delete language. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping("/{id}")
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteLanguage(
            @PathVariable Long id,
            Locale locale) {

        languageService.deleteById(id);

        String message = messageSource.getMessage("language.delete.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Update language", description = "Update language. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping("/{id}")
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateLanguage(
            @PathVariable Long id,
            @RequestBody @Valid LanguageInputDto languageInputDto,
            Locale locale) {

        languageService.updateLanguage(languageInputDto,id);

        String message = messageSource.getMessage("language.update.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }


}
