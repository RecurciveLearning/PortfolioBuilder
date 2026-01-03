package com.portfolio.service;

import com.portfolio.entity.*;
import com.portfolio.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock private UserProfileRepository userProfileRepository;
    @Mock private EducationRepository educationRepository;
    @Mock private ExperienceRepository experienceRepository;
    @Mock private SkillRepository skillRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private ContactMessageRepository contactMessageRepository;
    @Mock private MeetingRequestRepository meetingRequestRepository;

    @InjectMocks private PortfolioService portfolioService;

    @Test
    void getUserProfile_ShouldReturnProfile() {
        UserProfile profile = new UserProfile();
        profile.setName("Test");
        when(userProfileRepository.findAll()).thenReturn(Arrays.asList(profile));
        
        UserProfile result = portfolioService.getUserProfile();
        
        assertNotNull(result);
        assertEquals("Test", result.getName());
    }

    @Test
    void getAllEducation_ShouldReturnList() {
        Education edu = new Education();
        when(educationRepository.findAll()).thenReturn(Arrays.asList(edu));
        
        List<Education> result = portfolioService.getAllEducation();
        
        assertEquals(1, result.size());
    }

    @Test
    void saveEducation_ShouldSave() {
        Education edu = new Education();
        portfolioService.saveEducation(edu);
        verify(educationRepository).save(edu);
    }

    @Test
    void deleteEducation_ShouldDelete() {
        portfolioService.deleteEducation(1L);
        verify(educationRepository).deleteById(1L);
    }

    @Test
    void getAllExperience_ShouldReturnList() {
        Experience exp = new Experience();
        when(experienceRepository.findAll()).thenReturn(Arrays.asList(exp));
        
        List<Experience> result = portfolioService.getAllExperience();
        
        assertEquals(1, result.size());
    }

    @Test
    void getAllSkills_ShouldReturnList() {
        Skill skill = new Skill();
        when(skillRepository.findAll()).thenReturn(Arrays.asList(skill));
        
        List<Skill> result = portfolioService.getAllSkills();
        
        assertEquals(1, result.size());
    }

    @Test
    void getAllProjects_ShouldReturnList() {
        Project project = new Project();
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project));
        
        List<Project> result = portfolioService.getAllProjects();
        
        assertEquals(1, result.size());
    }

    @Test
    void saveContactMessage_ShouldSave() {
        ContactMessage msg = new ContactMessage();
        portfolioService.saveContactMessage(msg);
        verify(contactMessageRepository).save(any());
    }

    @Test
    void saveMeetingRequest_ShouldSave() {
        MeetingRequest req = new MeetingRequest();
        portfolioService.saveMeetingRequest(req);
        verify(meetingRequestRepository).save(any());
    }
}
