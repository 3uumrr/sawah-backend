package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.models.ChatConversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    Page<ChatConversation> findByUserIdOrderByUpdatedAtDesc(Long userId, Pageable pageable);

    Optional<ChatConversation> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("update ChatConversation c set c.updatedAt = :updatedAt where c.id = :conversationId")
    void updateUpdatedAtById(@Param("conversationId") Long conversationId, @Param("updatedAt") LocalDateTime updatedAt);
}
