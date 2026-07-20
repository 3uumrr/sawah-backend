package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.models.Notification;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final MessageSource messageSource;

    @Operation(summary = "List my notifications", description = "List my notifications. Required actor: authenticated user. Security constraint: isAuthenticated(). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Notification.class)))
    })
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<Notification>>> getMyNotifications(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PageableDefault(size = 20) Pageable pageable,
            Locale locale) {

        Page<Notification> notifications = notificationService.getUserNotifications(currentUser.getId(), pageable);
        return ResponseEntity.ok(new ApiResponse<>(
                messageSource.getMessage("common.success", null, locale),
                notifications,
                LocalDateTime.now()
        ));
    }

    @Operation(summary = "Count unread notifications", description = "Count unread notifications. Required actor: authenticated user. Security constraint: isAuthenticated(). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @GetMapping("/me/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Long>> countMyUnreadNotifications(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {

        long unreadCount = notificationService.countUnreadNotifications(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(
                messageSource.getMessage("common.success", null, locale),
                unreadCount,
                LocalDateTime.now()
        ));
    }

    @Operation(summary = "Mark notification as read", description = "Mark notification as read. Required actor: authenticated user. Security constraint: isAuthenticated(). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping("/{notificationId}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale) {

        notificationService.markAsRead(notificationId, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(
                messageSource.getMessage("notification.mark_read.success", null, locale),
                null,
                LocalDateTime.now()
        ));
    }
}
