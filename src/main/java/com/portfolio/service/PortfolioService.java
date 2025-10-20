package com.portfolio.service;

import com.portfolio.entity.*;
import com.portfolio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Autowired
    private MeetingRequestRepository meetingRequestRepository;

    // ------------------- UserProfile -------------------

    @Cacheable(value = "userProfile")
    public UserProfile getUserProfile() {
        return userProfileRepository.findAll().stream().findFirst().orElse(null);
    }

    @CachePut(value = "userProfile")
    public void saveUserProfile(UserProfile profile) {
        userProfileRepository.save(profile);
    }

    // ------------------- Education -------------------

    @Cacheable(value = "education")
    public List<Education> getAllEducation() {
        return educationRepository.findAll();
    }

    @CachePut(value = "education")
    public void saveEducation(Education education) {
        educationRepository.save(education);
    }

    @CacheEvict(value = "education", key = "#id")
    public void deleteEducation(Long id) {
        educationRepository.deleteById(id);
    }

    @Cacheable(value = "education", key = "#id")
    public Optional<Education> getEducationById(Long id) {
        return educationRepository.findById(id);
    }

    // ------------------- Experience -------------------

    @Cacheable(value = "experience")
    public List<Experience> getAllExperience() {
        return experienceRepository.findAll();
    }

    @CachePut(value = "experience")
    public void saveExperience(Experience experience) {
        experienceRepository.save(experience);
    }

    @CacheEvict(value = "experience", key = "#id")
    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }

    @Cacheable(value = "experience", key = "#id")
    public Optional<Experience> getExperienceById(Long id) {
        return experienceRepository.findById(id);
    }

    // ------------------- Skill -------------------

    @Cacheable(value = "skills")
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Cacheable(value = "skills", key = "#category")
    public List<Skill> getSkillsByCategory(String category) {
        return skillRepository.findByCategory(category);
    }

    @CachePut(value = "skills")
    public void saveSkill(Skill skill) {
        skillRepository.save(skill);
    }

    @CacheEvict(value = "skills", key = "#id")
    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }

    @Cacheable(value = "skills", key = "#id")
    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    // ------------------- Project -------------------

    @Cacheable(value = "projects")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Cacheable(value = "projects", key = "#tech")
    public List<Project> getProjectsByTech(String tech) {
        return projectRepository.findByTechStackContaining(tech);
    }

    @CachePut(value = "projects")
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    @CacheEvict(value = "projects", key = "#id")
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Cacheable(value = "projects", key = "#id")
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    // ------------------- Resume -------------------

    @Cacheable(value = "resume", key = "'latest'")
    public Resume getLatestResume() {
        return resumeRepository.findAll().stream().findFirst().orElse(null);
    }

    @Cacheable(value = "resume", key = "'latestByOrder'")
    public Resume getLatestResumeByOrder() {
        return resumeRepository.findTopByOrderByUpdatedAtDesc();
    }

    @CachePut(value = "resume", key = "#resume.id")
    public void saveResume(Resume resume) {
        resumeRepository.save(resume);
    }

    @CacheEvict(value = "resume", key = "#id")
    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
    }

    // ------------------- Contact & Meeting -------------------

    public void saveContactMessage(ContactMessage message) {
        message.setSentAt(LocalDateTime.now());
        contactMessageRepository.save(message);
    }

    public List<ContactMessage> getAllContactMessages() {
        return contactMessageRepository.findAll();
    }

    public void saveMeetingRequest(MeetingRequest request) {
        request.setCreatedAt(LocalDateTime.now());
        request.setStatus("PENDING");
        meetingRequestRepository.save(request);
    }

    public List<MeetingRequest> getAllMeetingRequests() {
        return meetingRequestRepository.findAll();
    }

    public List<MeetingRequest> getPendingMeetingRequests() {
        return meetingRequestRepository.findByStatus("PENDING");
    }

    public Optional<MeetingRequest> getMeetingRequestById(Long id) {
        return meetingRequestRepository.findById(id);
    }

    public void updateMeetingRequest(MeetingRequest request) {
        meetingRequestRepository.save(request);
    }
}
