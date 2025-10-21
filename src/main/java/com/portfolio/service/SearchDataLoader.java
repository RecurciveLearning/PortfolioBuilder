package com.portfolio.service;

import com.portfolio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchDataLoader {

    private final ProjectRepository projectRepo;
    private final SkillRepository skillRepo;
    private final EducationRepository eduRepo;
    private final ExperienceRepository expRepo;
    private final UserProfileRepository userRepo;


    public List <String> loadAllData() {
        List<String> data = new ArrayList<>();

        userRepo.findAll().forEach(u ->
                data.add(u.getName() + " " + u.getTitle() + " " + u.getAboutMe())
        );

        projectRepo.findAll().forEach(p ->
                data.add("Project: " + p.getTitle() + " " + p.getDescription() + " Tech: " + p.getTechStack())
        );

        skillRepo.findAll().forEach(s ->
                data.add("Skill: " + s.getSkillName() + " (" + s.getCategory() + ")")
        );

        eduRepo.findAll().forEach(e ->
                data.add("Education: " + e.getDegree() + " from " + e.getUniversity() + " (" + e.getYear() + ")")
        );

        expRepo.findAll().forEach(e ->
                data.add("Experience: " + e.getRole() + " at " + e.getCompany() + " - " + e.getDescription())
        );

        return data;
    }
}
