package com.portfolio.controller;

import com.portfolio.entity.*;
import com.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("profile", portfolioService.getUserProfile());
        model.addAttribute("education", portfolioService.getAllEducation());
        model.addAttribute("experiences", portfolioService.getAllExperience());
        model.addAttribute("skills", portfolioService.getAllSkills());
        model.addAttribute("projects", portfolioService.getAllProjects());
        model.addAttribute("contactMessages", portfolioService.getAllContactMessages());
        model.addAttribute("meetingRequests", portfolioService.getPendingMeetingRequests());
        model.addAttribute("resume", portfolioService.getLatestResume());
        return "admin/dashboard";
    }

    @PostMapping("/profile/save")
    public String saveProfile(@ModelAttribute UserProfile profile, RedirectAttributes redirectAttributes) {
        portfolioService.saveUserProfile(profile);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/education/save")
    public String saveEducation(@ModelAttribute Education education, RedirectAttributes redirectAttributes) {
        portfolioService.saveEducation(education);
        redirectAttributes.addFlashAttribute("success", "Education added successfully!");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/experience/save")
    public String saveExperience(@ModelAttribute Experience experience, RedirectAttributes redirectAttributes) {
        portfolioService.saveExperience(experience);
        redirectAttributes.addFlashAttribute("success", "Experience added successfully!");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/skill/save")
    public String saveSkill(@ModelAttribute Skill skill, RedirectAttributes redirectAttributes) {
        portfolioService.saveSkill(skill);
        redirectAttributes.addFlashAttribute("success", "Skill added successfully!");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/project/save")
    public String saveProject(@ModelAttribute Project project, RedirectAttributes redirectAttributes) {
        portfolioService.saveProject(project);
        redirectAttributes.addFlashAttribute("success", "Project added successfully!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/education/delete/{id}")
    public String deleteEducation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        portfolioService.deleteEducation(id);
        redirectAttributes.addFlashAttribute("success", "Education deleted successfully!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/experience/delete/{id}")
    public String deleteExperience(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        portfolioService.deleteExperience(id);
        redirectAttributes.addFlashAttribute("success", "Experience deleted successfully!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/skill/delete/{id}")
    public String deleteSkill(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        portfolioService.deleteSkill(id);
        redirectAttributes.addFlashAttribute("success", "Skill deleted successfully!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/project/delete/{id}")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        portfolioService.deleteProject(id);
        redirectAttributes.addFlashAttribute("success", "Project deleted successfully!");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/resume/upload")
    public String uploadResume(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload");
            return "redirect:/admin/dashboard";
        }
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/resume/";
            String fileName = "Himanshu_Resume.pdf";
            Path newFilePath = Paths.get(uploadDir + fileName);

            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            Resume oldResume = portfolioService.getLatestResume();
            if (oldResume != null) {
                Path oldFilePath = Paths.get(uploadDir + oldResume.getFileName());
                Files.deleteIfExists(oldFilePath);
                portfolioService.deleteResume(oldResume.getId());
            }
            Files.write(newFilePath, file.getBytes());
            Resume newResume = new Resume();
            newResume.setFileName(fileName);
           // newResume.setUpdatedAt(LocalDateTime.now());
            portfolioService.saveResume(newResume);
            redirectAttributes.addFlashAttribute("success", "Resume updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload resume: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }




    @PostMapping("/meeting/update/{id}")
    public String updateMeeting(@PathVariable Long id, @RequestParam String meetingLink, 
                                @RequestParam String status, RedirectAttributes redirectAttributes) {
        portfolioService.getMeetingRequestById(id).ifPresent(meeting -> {
            meeting.setMeetingLink(meetingLink);
            meeting.setStatus(status);
            portfolioService.updateMeetingRequest(meeting);
        });
        
        redirectAttributes.addFlashAttribute("success", "Meeting request updated successfully!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/error")
    public String adminError() {
        return "admin/error";
    }
}
