package com.codereaper.smartcodeassistant.model;

public class CodeRequest {
    private String prompt;

    public CodeRequest() {
    }

    public CodeRequest(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}