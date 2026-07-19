package com.sawah.sawah_backend.helper;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EmailVerificationService {

    private final WebClient webClient;

    @Value("${validate.email.api-key}")
    private String apiKey;


    public EmailVerificationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.hunter.io/v2").build();
    }

    public String getEmailStatus(String email) {
        try {
            JsonNode response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/email-verifier")
                            .queryParam("email", email)
                            .queryParam("api_key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block(); // متزامن عشان flow التسجيل

            if (response != null && response.has("data")) {
                return response.path("data").path("status").asText();
            }

            return "unknown";
        } catch (Exception e) {
            // لو الـ API ليميت خلص أو السيرفر هنج، بنرجع fallback عشان السيستم ميعطلش
            return "error";
        }
    }


}
