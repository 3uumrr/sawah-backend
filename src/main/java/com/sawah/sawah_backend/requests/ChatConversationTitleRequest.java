package com.sawah.sawah_backend.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatConversationTitleRequest(
        @NotBlank(message = "conversation.title.required")
        @Size(max = 200, message = "conversation.title.size")
        String title
) {
}
