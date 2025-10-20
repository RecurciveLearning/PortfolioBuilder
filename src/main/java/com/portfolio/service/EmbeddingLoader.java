package com.portfolio.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.entity.EmbeddingEntry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// other imports remain same

@Service
public class EmbeddingLoader {

    @Autowired
    private ResourceLoader resourceLoader;

    private List<EmbeddingEntry> embeddings;

    @PostConstruct
    public void loadEmbeddings() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Resource resource = resourceLoader.getResource("classpath:embeddings.json");
            embeddings = mapper.readValue(resource.getInputStream(), new TypeReference<List<EmbeddingEntry>>() {});
            System.out.println("✅ Loaded " + embeddings.size() + " embeddings from embeddings.json");
        } catch (IOException e) {
            System.err.println("❌ Failed to load embeddings: " + e.getMessage());
            embeddings = new ArrayList<>();
        }
    }

    @Cacheable("embeddingsCache")
    public List<EmbeddingEntry> getEmbeddings() {
        return embeddings != null ? embeddings : new ArrayList<>();
    }
}



