package com.sawah.sawah_backend.service.chatMessage;

import com.sawah.sawah_backend.enums.ChatSender;
import com.sawah.sawah_backend.models.ChatConversation;
import com.sawah.sawah_backend.models.ChatMessage;
import com.sawah.sawah_backend.repository.ChatMessageRepository;
import com.sawah.sawah_backend.requests.ChatMessageRequest;
import com.sawah.sawah_backend.service.chatConversation.ChatConversationService;
import com.sawah.sawah_backend.service.aiService.chatbot.AiChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final AiChatService aiChatService;
    private final ChatConversationService chatConversationService;

    @Override
    @Transactional
    public ChatMessage addMessage(ChatMessageRequest request, Long userId) {
        boolean isFirstMessage = (request.conversationId() == null);

        ChatConversation conversation = isFirstMessage
                ? createNewConversation(request.message(), userId)
                : chatConversationService.getById(request.conversationId(), userId);

        saveMessageToDb(request.message(), ChatSender.TOURIST, conversation);

        String aiResponse = aiChatService.generateResponse(request.message(), isFirstMessage);
        ChatMessage assistantMessage = saveMessageToDb(aiResponse, ChatSender.ASSISTANT, conversation);

        // Update timestamp via the specialized isolated service method to avoid Hibernate cache conflict
        chatConversationService.updateConversationTimestamp(conversation.getId());

        return assistantMessage;
    }

    @Override
    public Page<ChatMessage> findByChatConversationIdOrderByCreatedAtAsc(Long conversationId, Pageable pageable) {

        return chatMessageRepository.findByChatConversationIdOrderByCreatedAtAsc(conversationId, pageable);

    }


    private ChatConversation createNewConversation(String firstMessage, Long userId) {
        String aiGeneratedTitle = aiChatService.generateTitle(firstMessage);
        ChatConversation conversation = chatConversationService.createConversation(userId, aiGeneratedTitle);

        // String temporaryTitle = "New Chat";
       // ChatConversation conversation = chatConversationService.createConversation(userId, temporaryTitle);

        // Background Asynchronous Task
        // CompletableFuture.runAsync(() -> {
        //    try {
        //        Thread.sleep(2000); // Wait for main transaction to safely commit
        //        String aiGeneratedTitle = aiChatService.generateTitle(firstMessage);
        //        chatConversationService.updateTitle(conversation.getId(), aiGeneratedTitle, userId);
        //   } catch (Exception e) {
        //       log.error("Background title generation failed", e);
        //   }
        // });
        // String aiGeneratedTitle = aiChatService.generateTitle(firstMessage);
        // chatConversationService.updateTitle(conversation.getId(), aiGeneratedTitle, userId);
        return conversation;
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
