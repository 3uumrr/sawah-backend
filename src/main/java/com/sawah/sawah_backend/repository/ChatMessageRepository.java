package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.models.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = """
            SELECT cm
            FROM ChatMessage cm
            JOIN FETCH cm.chatConversation cc
            JOIN FETCH cc.user
            WHERE cc.id = :conversationId
            ORDER BY cm.createdAt ASC
            """,
            countQuery = """
            SELECT COUNT(cm)
            FROM ChatMessage cm
            WHERE cm.chatConversation.id = :conversationId
            """)
    Page<ChatMessage> findByChatConversationIdOrderByCreatedAtAsc(@Param("conversationId") Long conversationId, Pageable pageable);
}
