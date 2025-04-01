package com.codereaper.smartcodeassistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OllamaService {

    private final String ollamaUrl;
    private final String modelName;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OllamaService(
            @Value("${ollama.base-url:http://localhost:11434}") String ollamaUrl,
            @Value("${ollama.model:qwen2.5-coder:0.5b}") String modelName) {
        this.ollamaUrl = ollamaUrl;
        this.modelName = modelName;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String generateResponse(String prompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", modelName);

            // Create a chat message format
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content",
                    "You are a helpful coding assistant built by Qwen2.5. Provide detailed and helpful responses.");

            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);

            messages.add(systemMessage);
            messages.add(userMessage);

            requestBody.put("messages", messages);

            // Add parameters for a complete response
            requestBody.put("stream", false);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2000);

            String requestJson = objectMapper.writeValueAsString(requestBody);
            System.out.println("Sending request to Ollama: " + requestJson);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ollamaUrl + "/api/chat"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response from Ollama: " + response.body());

            if (response.statusCode() == 200) {
                Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);

                // Extract the message content from the response
                if (responseMap.containsKey("message")) {
                    Map<String, Object> message = (Map<String, Object>) responseMap.get("message");
                    if (message.containsKey("content")) {
                        return message.get("content").toString();
                    }
                }

                // Fallback to the response field if message format is not found
                Object responseText = responseMap.get("response");
                return responseText != null ? responseText.toString() : "No response generated";
            } else {
                return "Error: " + response.statusCode() + " - " + response.body();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error generating response: " + e.getMessage();
        }
    }
}