package com.sawah.sawah_backend.service.chatMessage;

import com.sawah.sawah_backend.models.ChatMessage;
import com.sawah.sawah_backend.requests.ChatMessageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageService {
    ChatMessage addMessage(ChatMessageRequest request , Long userId);
    Page<ChatMessage> findByChatConversationIdOrderByCreatedAtAsc(Long conversationId, Pageable pageable);
}
