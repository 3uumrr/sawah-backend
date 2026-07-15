package com.sawah.sawah_backend.controller;

import com.sawah.sawah_backend.dto.chatConversation.ChatConversationResponse;
import com.sawah.sawah_backend.dto.chatMessage.ChatMessageResponse;
import com.sawah.sawah_backend.mapper.ChatConversationMapper;
import com.sawah.sawah_backend.mapper.ChatMessageMapper;
import com.sawah.sawah_backend.models.ChatConversation;
import com.sawah.sawah_backend.requests.ChatConversationTitleRequest;
import com.sawah.sawah_backend.requests.ChatMessageRequest;
import com.sawah.sawah_backend.response.ApiResponse;
import com.sawah.sawah_backend.security.user.CustomUserDetails;
import com.sawah.sawah_backend.service.chatConversation.ChatConversationService;
import com.sawah.sawah_backend.service.chatMessage.ChatMessageService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ChatConversationService chatConversationService;
    private final ChatMessageMapper chatMessageMapper;
    private final ChatConversationMapper chatConversationMapper;
    private final MessageSource messageSource;

    @Operation(summary = "Send chat message", description = "Send chat message. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = ChatMessageResponse.class)))
    })
    @PostMapping("/messages")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> addMessage(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody @Valid ChatMessageRequest request,
            Locale locale) {

        ChatMessageResponse response = chatMessageMapper.toMessageResponse(
                chatMessageService.addMessage(request, currentUser.getId()));

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(message, response, LocalDateTime.now()));
    }

    @Operation(summary = "List chat conversations", description = "List chat conversations. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ChatConversationResponse.class)))
    })
    @GetMapping("/conversations")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Page<ChatConversationResponse>>> getConversations(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PageableDefault(size = 20) Pageable pageable,
            Locale locale) {

        Page<ChatConversationResponse> conversations = chatConversationService
                .findByUserIdOrderByUpdatedAtDesc(currentUser.getId(), pageable)
                .map(chatConversationMapper::toConversationResponse);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, conversations, LocalDateTime.now()));
    }

    @Operation(summary = "List conversation messages", description = "List conversation messages. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ChatMessageResponse.class)))
    })
    @GetMapping("/conversations/{conversationId}/messages")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Page<ChatMessageResponse>>> getConversationMessages(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long conversationId,
            @PageableDefault(size = 50) Pageable pageable,
            Locale locale) {

        ChatConversation conversation = chatConversationService.getById(conversationId, currentUser.getId());

        Page<ChatMessageResponse> messages = chatMessageService
                .findByChatConversationIdOrderByCreatedAtAsc(conversation.getId(), pageable)
                .map(chatMessageMapper::toMessageResponse);

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, messages, LocalDateTime.now()));
    }

    @Operation(summary = "Update conversation title", description = "Update conversation title. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PatchMapping("/conversations/{conversationId}")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> updateConversationTitle(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long conversationId,
            @RequestBody @Valid ChatConversationTitleRequest request,
            Locale locale) {

        chatConversationService.updateTitle(conversationId, request.title(), currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }

    @Operation(summary = "Delete conversation", description = "Delete conversation. Required actor: TOURIST. Security constraint: hasRole('TOURIST'). Successful responses are wrapped in the application ApiResponse envelope.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @DeleteMapping("/conversations/{conversationId}")
    @PreAuthorize("hasRole('TOURIST')")
    public ResponseEntity<ApiResponse<Void>> deleteConversation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long conversationId,
            Locale locale) {

        chatConversationService.deleteChatConversation(conversationId, currentUser.getId());

        String message = messageSource.getMessage("common.success", null, locale);

        return ResponseEntity.ok(new ApiResponse<>(message, null, LocalDateTime.now()));
    }
}
