package com.sawah.sawah_backend.service.aiService;

public interface AiChatService {
    String generateResponse(String prompt, boolean isFirstMessage);
    String generateTitle(String firstMessage);
}
