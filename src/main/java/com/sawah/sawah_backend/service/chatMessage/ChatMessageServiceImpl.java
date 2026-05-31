package com.sawah.sawah_backend.service.chatMessage;

import com.sawah.sawah_backend.enums.ChatSender;
import com.sawah.sawah_backend.models.ChatConversation;
import com.sawah.sawah_backend.models.ChatMessage;
import com.sawah.sawah_backend.repository.ChatMessageRepository;
import com.sawah.sawah_backend.requests.ChatMessageRequest;
import com.sawah.sawah_backend.service.chatConversation.ChatConversationService;
import com.sawah.sawah_backend.service.aiService.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final AiChatService aiChatService;
    private final ChatConversationService chatConversationService;

    @Override
    @Transactional
    public ChatMessage addMessage(ChatMessageRequest request, Long userId) {

        boolean isFirstMessage = (request.conversationId() == null);

        ChatConversation conversation = (request.conversationId() == null)
                ? createNewConversation(request.message(), userId)
                : chatConversationService.getById(request.conversationId(), userId);

        saveMessageToDb(request.message(), ChatSender.TOURIST, conversation);


        String aiResponse = aiChatService.generateResponse(request.message(), isFirstMessage);
        ChatMessage assistantMessage = saveMessageToDb(aiResponse, ChatSender.ASSISTANT, conversation);

        conversation.setUpdatedAt(LocalDateTime.now());

        return assistantMessage;

    }


    @Override
    public Page<ChatMessage> findByChatConversationIdOrderByCreatedAtAsc(Long conversationId, Pageable pageable) {

        return chatMessageRepository.findByChatConversationIdOrderByCreatedAtAsc(conversationId, pageable);

    }


    private ChatConversation createNewConversation(String firstMessage, Long userId) {

        String conversationTitle = aiChatService.generateTitle(firstMessage);

        return chatConversationService.createConversation(userId, conversationTitle);
    }

    private ChatMessage saveMessageToDb(String content, ChatSender sender, ChatConversation conversation) {
        ChatMessage message = ChatMessage.builder()
                .message(content)
                .sender(sender)
                .chatConversation(conversation)
                .build();
        return chatMessageRepository.save(message);
    }


}
