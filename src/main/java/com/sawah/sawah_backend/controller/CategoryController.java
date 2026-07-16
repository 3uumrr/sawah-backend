package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.category.CategoryInputDto;
import com.sawah.sawah_backend.dto.category.CategoryResponseDto;
import com.sawah.sawah_backend.mapper.CategoryMapper;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.service.category.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final MessageSource messageSource;
    private final CategoryMapper mapper;


    @Operation(summary = "List categories", description = "List categories. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CategoryResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories(Locale locale){

        List<CategoryResponseDto> categories = mapper.toListResponseDto(categoryService.getAllCategories());

        String message = messageSource.getMessage("common.success",null,locale);

        return ResponseEntity.ok(new ApiResponse<>(message, categories, LocalDateTime.now()));
    }

    @Operation(summary = "Get category by ID", description = "Get category by ID. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CategoryResponseDto.class)))
    })
    @GetMapping("/{id}")
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryById(@PathVariable Long id, Locale locale){

        CategoryResponseDto category = mapper.toResponseDto(categoryService.getCategoryById(id));

        String message = messageSource.getMessage("common.success",null,locale);

        return ResponseEntity.ok(new ApiResponse<>(message, category, LocalDateTime.now()));
    }

    @Operation(summary = "Create category", description = "Create category. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> addCategory(
            @RequestPart(name = "category") @Valid CategoryInputDto categoryInputDto,
            @RequestPart(required = true , name = "image") MultipartFile file,
            Locale locale){

        categoryService.addCategory(categoryInputDto,file);

        String message = messageSource.getMessage("common.success",null,locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Update category", description = "Update category. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
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
    public ResponseEntity<ApiResponse<Void>> updateCategory(
            @PathVariable Long id,
            @RequestPart(name = "category") @Valid CategoryInputDto categoryInputDto,
            @RequestPart(required = false , name = "image") MultipartFile file,
            Locale locale){

        categoryService.updateCategory(id, categoryInputDto,file);

        String message = messageSource.getMessage("category.update.success",null,locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Delete category", description = "Delete category. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping("/{id}")
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id, Locale locale){

        categoryService.deleteCategoryById(id);

        String message = messageSource.getMessage("category.delete.success",null,locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }


}
