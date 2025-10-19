package com.portfolio.controller;

import com.portfolio.entity.EmbeddingEntry;
import com.portfolio.service.EmbeddingService;
import com.portfolio.service.EmbeddingLoader;
import org.springframework.web.bind.annotation.*;
import ai.onnxruntime.OrtException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


import com.portfolio.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        try {
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

        } catch (NullPointerException e) {
            System.err.println("Chat NullPointerException: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "type", "error",
                    "response", "I encountered an error processing your request. Please try again."
            ));
        } catch (Exception e) {
            System.err.println("Chat error: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "type", "error",
                    "response", "I'm having trouble right now. Please try again later or contact me directly."
            ));
        }
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

