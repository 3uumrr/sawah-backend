package com.sawah.sawah_backend.requests;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequest(
        @NotBlank(message = "message.required")
        String message,

        Long conversationId
) {
}
