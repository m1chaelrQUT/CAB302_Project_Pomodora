package com.qut.cab302_project_pomodora.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qut.cab302_project_pomodora.config.OllamaConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OllamaClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private static OllamaClient client;

    public OllamaClient() {
        this.httpClient = HttpClient.newBuilder().build();
        this.objectMapper = new ObjectMapper();
    }

    public static synchronized OllamaClient getInstance() {
        if (client == null) {
            client = new OllamaClient();
        }
        return client;
    }

    public CompletableFuture<OllamaResponse> generate(String prompt, String model, String format) {
        OllamaGenerateRequest ollamaRequest = new OllamaGenerateRequest(model, prompt, false, format); // stream = false for simplicity
        try {
            String requestBody = objectMapper.writeValueAsString(ollamaRequest);
            System.out.println("The request body: " + requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OllamaConfig.OLLAMA_API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(httpResponse -> {
                        if (httpResponse.statusCode() == 200) {
                            try {
                                return objectMapper.readValue(httpResponse.body(), OllamaResponse.class);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException("Failed to parse Ollama response: " + e.getMessage(), e);
                            }
                        } else {
                            throw new RuntimeException("Ollama request failed with status " + httpResponse.statusCode() + ": " + httpResponse.body());
                        }
                    });

        } catch (JsonProcessingException e) {
            return CompletableFuture.failedFuture(new RuntimeException("Failed to serialize Ollama request: " + e.getMessage(), e));
        }
    }

    public static class OllamaGenerateRequest {
        public String model;
        public String prompt;
        public boolean stream;
        public String format;

        public OllamaGenerateRequest(String model, String prompt, boolean stream, String format) {
            this.model = model;
            this.prompt = prompt;
            this.stream = stream;
            if (format.equals("json")) {this.format = format;}
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OllamaResponse {
        public String model;
        public @JsonProperty("created_at") String createdAt;
        public String response;
        public boolean done;

        @Override
        public String toString() {
            return "OllamaResponse{" +
                    "model='" + model + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", response='" + response + '\'' +
                    ", done=" + done +
                    '}';
        }
    }
}