package com.portfolio.controller;

import com.portfolio.entity.EmbeddingEntry;
import com.portfolio.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final SearchService searchService;

    public ChatController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/message")
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");

        if (message == null || message.isBlank()) {
            return ResponseEntity.ok(Map.of(
                    "type", "text",
                    "response", "Please provide a valid message."
            ));
        }

        if (message.length() > 500) {
            return ResponseEntity.ok(Map.of(
                    "type", "text",
                    "response", "Your message is too long. Please keep it under 500 characters."
            ));
        }

        List<EmbeddingEntry> matches = searchService.searchByKeywords(message);
        String response = searchService.generateResponse(message, matches);

        return ResponseEntity.ok(Map.of(
                "type", "text",
                "response", response,
                "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "healthy",
                "service", "chat",
                "timestamp", String.valueOf(System.currentTimeMillis())
        ));
    }
}
