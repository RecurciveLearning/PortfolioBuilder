package com.portfolio.controller;

import com.portfolio.entity.EmbeddingEntry;
import com.portfolio.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private SearchService searchService;

    @Test
    void chat_WithValidQuery_ShouldReturnResponse() throws Exception {
        EmbeddingEntry entry = new EmbeddingEntry();
        entry.setText("Test response");
        when(searchService.searchByKeywords(anyString())).thenReturn(Arrays.asList(entry));
        when(searchService.generateResponse(anyString(), anyList())).thenReturn("Test response");
        
        mockMvc.perform(post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"query\":\"test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").exists());
    }

    @Test
    void chat_WithEmptyQuery_ShouldReturnError() throws Exception {
        mockMvc.perform(post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"query\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
