package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.service.ServiceInputDto;
import com.sawah.sawah_backend.models.Service;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.service.service.ServiceService;
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
@RequestMapping("${api.prefix}/services")
@RequiredArgsConstructor
public class ServicesController {

    private final ServiceService serviceService;
    private final MessageSource messageSource;

    @Operation(summary = "Get service by ID", description = "Get service by ID. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Service.class)))
    })
    @GetMapping("/{id}")
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Service>> getServiceById(@PathVariable Long id, Locale locale) {
        Service data = serviceService.getById(id);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, data, LocalDateTime.now()));
    }

    @Operation(summary = "List services", description = "List services. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Service.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<Service>>> getServices(Locale locale) {
        List<Service> services = serviceService.getServices();

        String message =  messageSource.getMessage("common.success", null, locale);

       return ResponseEntity.ok(new ApiResponse<>(message, services, LocalDateTime.now()));
    }

    @Operation(summary = "Create service", description = "Create service. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> addService(
            @RequestBody @Valid ServiceInputDto serviceInputDto,
            Locale locale) {

        serviceService.addServices(serviceInputDto);

        String message = messageSource.getMessage("service.add.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Delete service", description = "Delete service. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping("/{id}")
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteService(
            @PathVariable Long id,
            Locale locale) {

        serviceService.deleteById(id);

        String message = messageSource.getMessage("service.delete.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Update service", description = "Update service. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping("/{id}")
    @Tag(name = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateService(
            @PathVariable Long id,
            @RequestBody @Valid ServiceInputDto serviceInputDto,
            Locale locale) {

        serviceService.updateServices(serviceInputDto,id);

        String message = messageSource.getMessage("service.update.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }


}
