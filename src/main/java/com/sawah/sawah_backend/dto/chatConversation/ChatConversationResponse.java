package com.sawah.sawah_backend.dto.chatConversation;

import java.time.LocalDateTime;

public record ChatConversationResponse(
        Long id,
        String chatTitle
) {
}
