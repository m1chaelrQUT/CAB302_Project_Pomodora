package com.qut.cab302_project_pomodora.model;

import com.qut.cab302_project_pomodora.config.OllamaConfig;
import com.qut.cab302_project_pomodora.util.OllamaClient;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.CompletableFuture;

public class LLMService {
    private final OllamaClient ollamaClient;
    private String lastResponse = "";
    private StringProperty statusMessage = new SimpleStringProperty("");
    private final String model = OllamaConfig.DEFAULT_MODEL;

    public LLMService() {
        this.ollamaClient = OllamaClient.getInstance();
    }

    public CompletableFuture<String> getCompletion(String prompt, String format) {
        Platform.runLater(() -> {statusMessage.set("Sending request to ollama client");});
        return ollamaClient.generate(prompt, model, format)
                .thenApply(ollamaResponse -> {
                    if (ollamaResponse.done) {
                        lastResponse = ollamaResponse.response;
                        Platform.runLater(() -> {statusMessage.set("Success");});
                        System.out.println("Successfully completed request to ollama client");
                        return ollamaResponse.response;
                    } else {
                        Platform.runLater(() -> statusMessage.set("Failed"));
                        return "Error: incomplete response.";
                    }
                })
                .exceptionally(ex -> {
                    String errorMessage = "Error communicating with ollama client" + ex.getMessage();
                    System.err.println(errorMessage);
                    ex.printStackTrace();
                    Platform.runLater(() -> statusMessage.set(errorMessage));
                    return "Error: " + errorMessage;
                });
    }

    public String lastResponseProperty() {
        return lastResponse;
    }

    public String getLastResponse() {
        return lastResponse;
    }

    public StringProperty statusMessageProperty() {
        return statusMessage;
    }

    public StringProperty getStatusMessage() {
        return statusMessage;
    }
}
