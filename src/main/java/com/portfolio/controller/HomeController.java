package com.portfolio.controller;

import com.portfolio.entity.*;
import com.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private PortfolioService portfolioService;

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

    @GetMapping("/resume/download")
    public ResponseEntity<Resource> downloadResume() throws IOException {
        Resource resource = new ClassPathResource("static/resume/Himanshu_Resume.pdf");
        
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Himanshu_Resume.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
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
}
