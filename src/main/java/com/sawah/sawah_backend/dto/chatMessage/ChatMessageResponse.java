package com.sawah.sawah_backend.dto.chatMessage;

import com.sawah.sawah_backend.enums.ChatSender;
import java.time.LocalDateTime;

public record ChatMessageResponse(
        ChatSender sender,
        String message,
        String userImageUrl,
        Long conversationId,
        LocalDateTime createdAt
)
{}
