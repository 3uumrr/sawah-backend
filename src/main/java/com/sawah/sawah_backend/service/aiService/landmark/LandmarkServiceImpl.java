package com.sawah.sawah_backend.service.aiService.landmark;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.MapOutputConverter; // استخدمنا دا عشان يرجعلنا JSON/Map جاهز
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LandmarkServiceImpl implements LandmarkService {

    private final ChatModel chatModel;
    private final WebClient webClient;

    @Override
    public String exploreLandmarkFromImage(MultipartFile file, Locale locale) {

        String landmarkName = extractLandmarkName(file);
        String targetLanguage = locale.getDisplayLanguage(Locale.ENGLISH);

        // 1. هنسيب Spring AI يشكل صيغة الـ JSON المطلوبة ويجبر الموديل عليها
        MapOutputConverter outputConverter = new MapOutputConverter();

        PromptTemplate promptTemplate = getPromptTemplate(targetLanguage, landmarkName, outputConverter);

        Prompt finalPrompt = promptTemplate.create();

        // 3. نداء الـ AI واستقبال الرد النضيف
        ChatResponse response = chatModel.call(finalPrompt);

        // رجع النص الصافي (هيكون JSON نضيف ومضمون 100% بدون ```json)
        return response.getResult().getOutput().getText();
    }

    private static @NonNull PromptTemplate getPromptTemplate(String targetLanguage, String landmarkName, MapOutputConverter outputConverter) {
        String promptText = """
                You are an expert tour guide assistant for an intelligent tourism system called "Sawah".
                Your task is to provide detailed and engaging information about a given landmark name.
                
                CRITICAL: All values in the JSON MUST be written strictly in the {targetLanguage} language.
                The landmark name is: {landmarkName}
                
                {format}
                """;

        // 2. دمج المتغيرات جوه الـ PromptTemplate
        PromptTemplate promptTemplate = new PromptTemplate(promptText);
        promptTemplate.add("targetLanguage", targetLanguage);
        promptTemplate.add("landmarkName", landmarkName);
        promptTemplate.add("format", outputConverter.getFormat()); // دي السحر اللي بيجبر الـ AI يبعت الـ Format صح من غير Markdown
        return promptTemplate;
    }

    private String extractLandmarkName(MultipartFile file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());

        MultiValueMap<String, ?> multipartBody = builder.build();

        JsonNode responseJson = this.webClient.post()
                .uri("/api/predict")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(multipartBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (responseJson != null && responseJson.has("landmark")) {
            return responseJson.get("landmark").asText();
        }

        throw new RuntimeException("landmark.extraction.failed");
    }
}