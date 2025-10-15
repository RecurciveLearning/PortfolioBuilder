package com.portfolio.controller;

import com.portfolio.entity.EmbeddingEntry;
import com.portfolio.service.EmbeddingService;
import com.portfolio.service.EmbeddingLoader;
import org.springframework.web.bind.annotation.*;
import ai.onnxruntime.OrtException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final EmbeddingService embeddingService;
    private final EmbeddingLoader embeddingLoader;

    public ChatController(EmbeddingService embeddingService, EmbeddingLoader embeddingLoader) {
        this.embeddingService = embeddingService;
        this.embeddingLoader = embeddingLoader;
    }

    @PostMapping("/message")
    public Map<String, Object> chat(@RequestBody Map<String, String> payload) throws OrtException {
        String message = payload.get("message");
        if (message == null || message.isBlank()) {
            return Map.of("type", "text", "response", "Please provide a valid message.");
        }

        // TODO: replace with dynamic tokenization
        long[][] tokenizedInput = new long[1][384]; // Example dummy input, replace with real tokenizer
        float[] queryEmbedding = embeddingService.getQueryEmbedding(tokenizedInput);

        List<EmbeddingEntry> topMatches = embeddingLoader.getEmbeddings().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e, cosineSimilarity(queryEmbedding, e.getEmbedding())))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        StringBuilder reply = new StringBuilder();
        for (EmbeddingEntry e : topMatches) {
            reply.append(e.getType()).append(": ").append(e.getText()).append("\n\n");
        }
        if (reply.isEmpty()) reply.append("Sorry, I couldn't find anything relevant.");

        return Map.of("type", "text", "response", reply.toString());
    }

    private double cosineSimilarity(float[] vec1, List<Double> vec2List) {
        if (vec1 == null || vec2List == null || vec1.length != vec2List.size()) return 0.0;
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            double v2 = vec2List.get(i);
            dot += vec1[i] * v2;
            normA += vec1[i] * vec1[i];
            normB += v2 * v2;
        }
        return normA == 0 || normB == 0 ? 0.0 : dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
