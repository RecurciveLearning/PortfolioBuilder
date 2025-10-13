package com.portfolio.service;

import com.portfolio.entity.*;
import com.portfolio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    public UserProfile getUserProfile() {
        return userProfileRepository.findAll().stream().findFirst().orElse(null);
    }

    public List<Education> getAllEducation() {
        return educationRepository.findAll();
    }

    public List<Experience> getAllExperience() {
        return experienceRepository.findAll();
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public List<Skill> getSkillsByCategory(String category) {
        return skillRepository.findByCategory(category);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsByTech(String tech) {
        return projectRepository.findByTechStackContaining(tech);
    }

    public Resume getLatestResume() {
        return resumeRepository.findAll().stream()
                .findFirst().orElse(null);
    }

    public void saveContactMessage(ContactMessage message) {
        message.setSentAt(LocalDateTime.now());
        contactMessageRepository.save(message);
    }

    public void saveMeetingRequest(MeetingRequest request) {
        request.setCreatedAt(LocalDateTime.now());
        request.setStatus("PENDING");
        meetingRequestRepository.save(request);
    }

    public List<ContactMessage> getAllContactMessages() {
        return contactMessageRepository.findAll();
    }

    public List<MeetingRequest> getAllMeetingRequests() {
        return meetingRequestRepository.findAll();
    }

    public List<MeetingRequest> getPendingMeetingRequests() {
        return meetingRequestRepository.findByStatus("PENDING");
    }

    public void saveUserProfile(UserProfile profile) {
        userProfileRepository.save(profile);
    }

    public void saveEducation(Education education) {
        educationRepository.save(education);
    }

    public void saveExperience(Experience experience) {
        experienceRepository.save(experience);
    }

    public void saveSkill(Skill skill) {
        skillRepository.save(skill);
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public void saveResume(Resume resume) {
        resumeRepository.save(resume);
    }

    public void deleteEducation(Long id) {
        educationRepository.deleteById(id);
    }

    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }

    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public Optional<Education> getEducationById(Long id) {
        return educationRepository.findById(id);
    }

    public Optional<Experience> getExperienceById(Long id) {
        return experienceRepository.findById(id);
    }

    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Optional<MeetingRequest> getMeetingRequestById(Long id) {
        return meetingRequestRepository.findById(id);
    }

    public void updateMeetingRequest(MeetingRequest request) {
        meetingRequestRepository.save(request);
    }

    public Resume getLatestResumeByOrder() {
        return resumeRepository.findTopByOrderByUpdatedAtDesc(); // assuming JPA repository
    }

    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
    }
}
