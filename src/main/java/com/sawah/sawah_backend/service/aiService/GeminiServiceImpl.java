package com.sawah.sawah_backend.service.aiService;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiServiceImpl implements AiChatService {

    private final ChatModel chatModel;

    @Override
    public String generateResponse(String userPrompt, boolean isFirstMessage) {
        String roleContext = "You are a professional tour guide for the 'Sawah' (سواح) app. " +
                "Crucial: Always respond in the SAME LANGUAGE as the user's prompt. ";

        String behavior = isFirstMessage
                ? "If the prompt is in Arabic, welcome them warmly in Egyptian dialect. If in English, welcome them professionally. "
                : "Answer the question directly and concisely without any greetings or introductions. ";

        return chatModel.call(roleContext + behavior + "\nUser Prompt: " + userPrompt);
    }

    @Override
    public String generateTitle(String firstMessage) {
        String systemInstruction = "You are a travel assistant. Summarize the following message into a catchy 3-word title. " +
                "The title MUST be in the same language as the message (Arabic or English): ";

        return chatModel.call(systemInstruction + firstMessage)
                .replace("\"", "")
                .trim();
    }
}