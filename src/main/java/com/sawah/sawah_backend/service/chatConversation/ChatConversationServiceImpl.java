package com.sawah.sawah_backend.service.chatConversation;

import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.ChatConversation;
import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.repository.ChatConversationRepository;
import com.sawah.sawah_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatConversationServiceImpl implements  ChatConversationService {

    private final ChatConversationRepository chatConversationRepository;
    private final UserService userService;

    @Override
    public ChatConversation getById(Long id, Long userId) {
        return chatConversationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatConversation.not.found"));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ChatConversation createConversation(Long userId, String title) {

        User user = userService.getUserById(userId);

        ChatConversation conversation = ChatConversation.builder()
                .chatTitle(title)
                .user(user)
                .build();

        return chatConversationRepository.save(conversation);
    }

    @Override
    @Transactional
    public void updateConversationTimestamp(Long conversationId) {
        chatConversationRepository.updateUpdatedAtById(conversationId, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void updateTitle(Long conversationId, String title , Long userId) {
        ChatConversation conversation = getById(conversationId,userId);

        conversation.setChatTitle(title);
    }

    @Override
    @Transactional
    public void deleteChatConversation(Long id , Long userId) {
        ChatConversation conversation = getById(id,userId);

        chatConversationRepository.delete(conversation);
    }

    @Override
    public Page<ChatConversation> findByUserIdOrderByUpdatedAtDesc(Long userId, Pageable pageable) {
        return chatConversationRepository.findByUserIdOrderByUpdatedAtDesc(userId, pageable);
    }
}
