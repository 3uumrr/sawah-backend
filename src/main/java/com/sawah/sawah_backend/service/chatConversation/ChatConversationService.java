package com.sawah.sawah_backend.service.chatConversation;

import com.sawah.sawah_backend.models.ChatConversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatConversationService {
    ChatConversation getById(Long id , Long userId);
    ChatConversation createConversation(Long userId, String title);
    void updateConversationTimestamp(Long conversationId);
    void updateTitle(Long conversationId, String title , Long userId);
    void deleteChatConversation(Long chatConversationId , Long userId);
    Page<ChatConversation> findByUserIdOrderByUpdatedAtDesc(Long userId, Pageable pageable);
}
