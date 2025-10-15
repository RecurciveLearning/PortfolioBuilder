package com.portfolio.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.entity.EmbeddingEntry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class EmbeddingLoader {

    private List<EmbeddingEntry> embeddings;

    @PostConstruct
    public void loadEmbeddings() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/pythonscript/embeddings.json");
            if (inputStream == null) {
                Path externalPath = Path.of("pythonscript/embeddings.json"); // fallback path
                if (Files.exists(externalPath)) {
                    inputStream = Files.newInputStream(externalPath);
                } else {
                    throw new RuntimeException("embeddings.json not found in resources or pythonscript folder!");
                }
            }
            embeddings = mapper.readValue(inputStream, new TypeReference<>() {});
            System.out.println("✅ Loaded " + embeddings.size() + " embeddings from embeddings.json");
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to load embeddings: " + e.getMessage(), e);
        }
    }

    public List<EmbeddingEntry> getEmbeddings() {
        return embeddings;
    }
}
