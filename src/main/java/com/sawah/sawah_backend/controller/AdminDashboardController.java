package com.sawah.sawah_backend.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.MessageSource;
import com.sawah.sawah_backend.dto.dashboard.DashboardCounters;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/admin/dashboard")
@RequiredArgsConstructor
@Tag(name = "Admin Dashboard", description = "Endpoints for Admin Dashboard Overview and Analytics")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;
    private final MessageSource messageSource;

    @GetMapping("/overview")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = DashboardCounters.class)))
    })
    public ResponseEntity<ApiResponse<DashboardCounters>> getDashboardOverview(Locale locale) {

        DashboardCounters overviewData = adminDashboardService.getOverviewData();

        String message = messageSource.getMessage("admin.dashboard.load_success", null, locale);


        return ResponseEntity.ok(new ApiResponse<>(message, overviewData, LocalDateTime.now()));
    }
}
