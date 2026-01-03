package com.portfolio.controller;

import com.portfolio.entity.*;
import com.portfolio.service.CertificationService;
import com.portfolio.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private PortfolioService portfolioService;
    @MockBean private CertificationService certificationService;

    @Test
    void home_ShouldReturnIndexPage() throws Exception {
        when(portfolioService.getUserProfile()).thenReturn(new UserProfile());
        when(portfolioService.getAllEducation()).thenReturn(Collections.emptyList());
        when(portfolioService.getAllExperience()).thenReturn(Collections.emptyList());
        when(portfolioService.getAllSkills()).thenReturn(Collections.emptyList());
        when(portfolioService.getAllProjects()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void submitContact_ShouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\",\"email\":\"test@test.com\",\"message\":\"Hello\"}"))
                .andExpect(status().isOk());
        
        verify(portfolioService).saveContactMessage(any());
    }

    @Test
    void submitMeetingRequest_ShouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/meeting/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\",\"email\":\"test@test.com\"}"))
                .andExpect(status().isOk());
        
        verify(portfolioService).saveMeetingRequest(any());
    }

    @Test
    void certificationsPage_ShouldReturnView() throws Exception {
        mockMvc.perform(get("/certifications"))
                .andExpect(status().isOk())
                .andExpect(view().name("certifications"));
    }

    @Test
    void listPublicCertifications_ShouldReturnJson() throws Exception {
        Certification cert = new Certification();
        cert.setName("Test Cert");
        when(certificationService.getAllCertifications()).thenReturn(Arrays.asList(cert));
        
        mockMvc.perform(get("/certifications/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Cert"));
    }
}
