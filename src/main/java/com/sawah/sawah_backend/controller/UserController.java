package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.user.CompleteTouristProfileDto;
import com.sawah.sawah_backend.dto.user.UpdateUserDto;
import com.sawah.sawah_backend.dto.user.UserAdminResponseDto;
import com.sawah.sawah_backend.dto.user.UserResponseDto;
import com.sawah.sawah_backend.mapper.UserMapper;
import com.sawah.sawah_backend.requests.ChangePasswordRequest;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageSource messageSource;
    private final UserMapper mapper;

    @Operation(summary = "Get user by ID", description = "Get user by ID. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserAdminResponseDto.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin")
    public ResponseEntity<ApiResponse<UserAdminResponseDto>> getUserById(
            @PathVariable Long id,
            Locale locale){

        UserAdminResponseDto response = mapper.toAdminResponseDto(userService.getUserById(id));

        String message = messageSource.getMessage("common.success",null,locale);

        return ResponseEntity
                .ok(new ApiResponse<>(message, response, LocalDateTime.now()));

    }

    @Operation(summary = "Search users", description = "Search users. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserAdminResponseDto.class)))
    })
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin")
    public ResponseEntity<ApiResponse<Page<UserAdminResponseDto>>> searchUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String email,
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            Locale locale) throws BadRequestException {

        Page<UserAdminResponseDto> data;
        if (email != null) {
            data = new PageImpl<>(List.of(mapper.toAdminResponseDto(userService.findUserByEmail(email))), pageable, 1);
        } else if (role != null) {
            data = userService.getUsersByRoleName(role, pageable)
                    .map(mapper::toAdminResponseDto);
        } else {
            throw new BadRequestException("search.params.missing");
        }

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, data, LocalDateTime.now()));
    }

    @Operation(summary = "Delete user by admin", description = "Delete user by admin. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin")
    public ResponseEntity<ApiResponse<Void>> deleteUserByAdmin(
            @PathVariable Long id,
            Locale locale){

        userService.deleteUser(id);

        String message = messageSource.getMessage("common.success",null,locale);

        return ResponseEntity
                .ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Delete my account", description = "Delete my account. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale){

        Long userId = currentUser.getId();

        userService.deleteUser(userId);

        String message = messageSource.getMessage("common.success",null,locale);

        return ResponseEntity
                .ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Toggle account status", description = "Toggle account status. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PatchMapping("/{id}/account-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin")
    public ResponseEntity<ApiResponse<Void>> changeAccountStatus(
            @PathVariable Long id,
            Locale locale
    ) {

        userService.changeAccountStatus(id);

        String message = messageSource.getMessage("common.success",null,locale);

        return ResponseEntity
                .status(HttpStatus.OK).body(
                        new ApiResponse<>(message, null, LocalDateTime.now()));

    }

    @Operation(summary = "List users", description = "List users. Required actor: ADMIN. Security constraint: hasRole('ADMIN'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserAdminResponseDto.class)))
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Admin")
    public ResponseEntity<ApiResponse<Page<UserAdminResponseDto>>> getAllUsers(
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            Locale locale
    ){

        Page<UserAdminResponseDto> response = userService.getAllUsers(pageable)
                .map(mapper::toAdminResponseDto);

        String message = messageSource.getMessage("common.success",null,locale);

        return ResponseEntity
                .ok(new ApiResponse<>(message, response, LocalDateTime.now()));

    }


    /*@PatchMapping(
            value = "/profile-photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('PROVIDER', 'TOURIST')")
    public ResponseEntity<ApiResponse<Void>> updateUserProfilePhoto(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestPart(required = true , name = "image") MultipartFile file,
            Locale locale){

        userService.changeProfilePhoto(currentUser.getId(),file);

        String message = messageSource.getMessage("user.photo.success",null,locale);

        return ResponseEntity
                .ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }*/

    @Operation(summary = "Update my user profile", description = "Update my user profile. Security constraint: hasAnyRole('PROVIDER', 'TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping(
            value = "/me",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('PROVIDER', 'TOURIST')")
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestPart @Valid UpdateUserDto updateUserDto,
            @RequestPart(required = false , name = "image") MultipartFile file,
            Locale locale){

        userService.updateUser(updateUserDto,currentUser.getId(),file);

        String message = messageSource.getMessage("user.profile.update.success",null,locale);

        return ResponseEntity
                .ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }


    @Operation(summary = "Change password", description = "Change password. Security constraint: hasAnyRole('PROVIDER', 'TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PatchMapping("/change-password")
    @PreAuthorize("hasAnyRole('PROVIDER', 'TOURIST')")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody @Valid ChangePasswordRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale){

        userService.updatePassword(request,currentUser.getId());

        String message = messageSource.getMessage("password.change.success",null,locale);

        return ResponseEntity
                .ok(new ApiResponse<>(message, null, LocalDateTime.now()));

    }

    @Operation(summary = "Get my tourist profile", description = "Get my tourist profile. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))
    })
    @GetMapping("/me")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<UserResponseDto>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale
    ) {

        UserResponseDto profile = mapper.toResponseDto(userService.getUserById(currentUser.getId()));

        String message = messageSource.getMessage("profile.fetch.success",null,locale);

        return ResponseEntity.ok(new ApiResponse<>(message, profile, LocalDateTime.now()));
    }

    @Operation(summary = "Change preferred language", description = "Change preferred language. Public endpoint; authentication is not enforced by method-level security. Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PatchMapping("/preferred-language")
    public ResponseEntity<ApiResponse<Void>> changePreferredLanguage(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Locale locale
    ) {

        userService.changePreferredLanguage(currentUser.getId());

        String message = messageSource.getMessage("common.success",null,locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Complete tourist profile", description = "Complete tourist profile. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PutMapping(value = "/complete-profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> completeProfile(
            @RequestPart(name = "user") @Valid CompleteTouristProfileDto completeTouristProfileDto,
            @RequestPart(required = false , name = "image") MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails,
            Locale locale

    ){
        userService.completeProfile(completeTouristProfileDto,image,userDetails.getUsername());

        String message = messageSource.getMessage("common.success",null,locale);

        return ResponseEntity
                .ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

}
