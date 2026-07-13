package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.issue.IssueInputDto;
import com.sawah.sawah_backend.dto.issue.IssueAdminResponseDto;
import com.sawah.sawah_backend.dto.issue.IssueResponseDto;
import com.sawah.sawah_backend.enums.IssueStatus;
import com.sawah.sawah_backend.mapper.IssueMapper;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.issue.IssueService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final IssueMapper issueMapper;
    private final MessageSource messageSource;

    @Operation(summary = "List issues for admin", description = "List issues for admin. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = IssueAdminResponseDto.class)))
    })
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin")
    public ResponseEntity<ApiResponse<List<IssueAdminResponseDto>>> getAllIssues(
            @RequestParam(required = false) IssueStatus status,
            Locale locale
    ) {
        List<IssueAdminResponseDto> response = issueMapper
                .issueToListIssueAdminDto(issueService.findAllIssues(status));

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, response, LocalDateTime.now()));
    }


    @Operation(summary = "List my issues", description = "List my issues. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = IssueResponseDto.class)))
    })
    @GetMapping("/me")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<List<IssueResponseDto>>> getMyIssues(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) IssueStatus status,
            Locale locale
    ) {

        List<IssueResponseDto> response;
        if (status == null) {
            response = issueMapper
                    .issueToListIssueDto(issueService.findByTouristId(currentUser.getId()));
        } else {
            response = issueMapper
                    .issueToListIssueDto(issueService.findByTouristIdAndStatus(currentUser.getId(), status));
        }


        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, response, LocalDateTime.now()));
    }

    @Operation(summary = "Create issue", description = "Create issue. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> createIssue(
            @RequestBody @Valid IssueInputDto issueInputDto,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale
    ) {
        issueService.createIssue(issueInputDto, currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Update issue status", description = "Update issue status. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PatchMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin")
    public ResponseEntity<ApiResponse<Void>> updateIssue(
            @RequestParam String issueNumber,
            @RequestParam IssueStatus status,
            Locale locale
    ) {
        issueService.updateIssue(issueNumber, status);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(message, null, LocalDateTime.now()));
    }
}
