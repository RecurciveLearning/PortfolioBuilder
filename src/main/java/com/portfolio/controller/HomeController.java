package com.portfolio.controller;

import com.portfolio.entity.*;
import com.portfolio.repository.BlogRepository;
import com.portfolio.service.CertificationService;
import com.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private CertificationService certificationService;


    private static final String RESUME_PATH = "static/resume/Himanshu_Resume.pdf";


    @GetMapping("/")
    public String home(Model model) {
        UserProfile profile = portfolioService.getUserProfile();
        List<Education> education = portfolioService.getAllEducation();
        List<Experience> experiences = portfolioService.getAllExperience();
        List<Skill> skills = portfolioService.getAllSkills();
        List<Project> projects = portfolioService.getAllProjects();
        Resume resume = portfolioService.getLatestResume();

        Map<String, List<Skill>> skillsByCategory = skills.stream()
                .collect(Collectors.groupingBy(Skill::getCategory));

        model.addAttribute("profile", profile);
        model.addAttribute("education", education);
        model.addAttribute("experiences", experiences);
        model.addAttribute("skillsByCategory", skillsByCategory);
        model.addAttribute("projects", projects);
        model.addAttribute("resume", resume);

        return "index";
    }

    @GetMapping("/resume/preview")
    public ResponseEntity<Resource> previewResume() throws IOException {
        Resource resource = new ClassPathResource("static/resume/Himanshu_Resume.pdf");

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=Himanshu_Resume.pdf")
                .body(resource);
    }

    @GetMapping("/resume/download")
    public ResponseEntity<Resource> downloadResume() throws IOException {
        Resource resource = new ClassPathResource("static/resume/Himanshu_Resume.pdf");

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Himanshu_Resume.pdf")
                .body(resource);
    }


    @PostMapping("/contact")
    @ResponseBody
    public ResponseEntity<String> submitContact(@RequestBody ContactMessage message) {
        portfolioService.saveContactMessage(message);
        return ResponseEntity.ok("Message sent successfully!");
    }

    @PostMapping("/meeting/request")
    @ResponseBody
    public ResponseEntity<String> submitMeetingRequest(@RequestBody MeetingRequest request) {
        portfolioService.saveMeetingRequest(request);
        return ResponseEntity.ok("Meeting request submitted successfully!");
    }

    @GetMapping("/certifications")
    public String certificationsPage() {
        return "certifications"; // certifications.html
    }

    // ✅ New: Public API for certifications
    @GetMapping("/certifications/list")
    @ResponseBody
    public List<Map<String, Object>> listPublicCertifications() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        List<Map<String, Object>> result = new ArrayList<>();
        for (Certification cert : certificationService.getAllCertifications()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cert.getId());
            map.put("name", cert.getName());
            map.put("organization", cert.getOrganization());
            map.put("issueDate", cert.getIssueDate() != null ? cert.getIssueDate().format(formatter) : "");
            map.put("verified", cert.isVerified());
            map.put("certificateUrl", cert.getCertificateUrl());
            result.add(map);
        }
        return result;
    }

}
