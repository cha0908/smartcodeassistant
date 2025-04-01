package com.codereaper.smartcodeassistant.controller;

import com.codereaper.smartcodeassistant.model.CodeRequest;
import com.codereaper.smartcodeassistant.model.CodeResponse;
import com.codereaper.smartcodeassistant.service.OllamaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
public class CodeAssistantController {

    private final OllamaService ollamaService;

    public CodeAssistantController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @PostMapping("/generate")
    public ResponseEntity<CodeResponse> generateCode(@RequestBody CodeRequest request) {
        try {
            String response = ollamaService.generateResponse(request.getPrompt());
            return ResponseEntity.ok(new CodeResponse(response));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new CodeResponse("Error processing your request: " + e.getMessage()));
        }
    }
}