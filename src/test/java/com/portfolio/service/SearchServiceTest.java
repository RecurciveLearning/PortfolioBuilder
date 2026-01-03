package com.portfolio.service;

import com.portfolio.entity.EmbeddingEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock private EmbeddingLoader embeddingLoader;
    @InjectMocks private SearchService searchService;

    @BeforeEach
    void setUp() {
        EmbeddingEntry entry = new EmbeddingEntry();
        entry.setType("experience");
        entry.setText("Java Developer with 3 years experience");
        when(embeddingLoader.getEmbeddings()).thenReturn(Arrays.asList(entry));
    }

    @Test
    void searchByKeywords_WithValidQuery_ShouldReturnResults() {
        List<EmbeddingEntry> results = searchService.searchByKeywords("java developer");
        assertNotNull(results);
    }

    @Test
    void searchByKeywords_WithEmptyQuery_ShouldReturnEmpty() {
        List<EmbeddingEntry> results = searchService.searchByKeywords("");
        assertTrue(results.isEmpty());
    }

    @Test
    void searchByKeywords_WithNullQuery_ShouldReturnEmpty() {
        List<EmbeddingEntry> results = searchService.searchByKeywords(null);
        assertTrue(results.isEmpty());
    }

    @Test
    void generateResponse_WithMatches_ShouldReturnResponse() {
        EmbeddingEntry entry = new EmbeddingEntry();
        entry.setType("experience");
        entry.setText("Java Developer");
        
        String response = searchService.generateResponse("java", Arrays.asList(entry));
        
        assertNotNull(response);
        assertTrue(response.contains("experience") || response.contains("Java"));
    }

    @Test
    void generateResponse_WithNoMatches_ShouldReturnDefault() {
        String response = searchService.generateResponse("test", Collections.emptyList());
        assertNotNull(response);
    }

    @Test
    void getCachedEmbeddings_ShouldReturnList() {
        List<EmbeddingEntry> results = searchService.getCachedEmbeddings();
        assertNotNull(results);
    }
}
