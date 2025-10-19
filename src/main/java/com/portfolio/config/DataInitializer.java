package com.portfolio.config;

import com.portfolio.entity.*;
import com.portfolio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

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
    private  BlogRepository blogRepository;

    @Autowired
    private  CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (userProfileRepository.count() == 0) {
            initializeData();
        }


    }
    private void initializeData() {
        UserProfile profile = new UserProfile();
        profile.setName("Himanshu Singh");
        profile.setTitle("Java Backend Developer | Spring Boot | Microservices");
        profile.setAboutMe("Backend Developer with 3.1 years of experience building scalable web applications using Java, Spring Boot, Microservices, and Kafka. Proficient in designing and implementing RESTful APIs, optimizing database performance, and integrating third-party services. Experienced in deploying applications on AWS and delivering secure, high-performance backend solutions using Agile methodologies and cloud-native architectures.");
        profile.setEmail("se.himanshusingh@gmail.com");
        profile.setPhone("+91 9740454280");
        profile.setCity("Bengaluru, India");
        profile.setLinkedin("https://linkedin.com/in/himanshusingh");
        profile.setGithub("https://github.com/himanshusingh");
        profile.setProfileImage("/images/profile.jpg");
        userProfileRepository.save(profile);

        Education education = new Education();
        education.setDegree("B. Tech. (Computer Science & Engineering)");
        education.setUniversity("Punjabi University, Patiala");
        education.setYear("2021");
        educationRepository.save(education);

        Experience experience = new Experience();
        experience.setCompany("Terralogic Software Solutions");
        experience.setRole("Java/J2EE Developer");
        experience.setStartDate(LocalDate.of(2022, 10, 1));
        experience.setDescription("Built RESTful microservices using Spring Boot, Spring Cloud, and Eureka. Implemented JWT-based authentication and Spring Security with RBAC. Developed microservices capable of handling both synchronous and asynchronous communications. Designed efficient database schemas using ER diagrams and optimized queries with Hibernate/JPA, reducing latency by 20%. Integrated third-party APIs for SMS, WhatsApp, and Email notifications. Achieved 85%+ code coverage with JUnit and Mockito tests.");
        experienceRepository.save(experience);

        String[] coreSkills = {"Core Java", "Java 1.8", "OOPs Concepts", "Spring Boot", "Spring MVC", "Spring Security", "Spring Cloud", "Hibernate/JPA", "MySQL", "Spring Data JPA", "JDBC"};
        for (String skill : coreSkills) {
            Skill s = new Skill();
            s.setSkillName(skill);
            s.setCategory("Programming Languages & Frameworks");
            s.setLevel(90);
            skillRepository.save(s);
        }

        String[] toolsSkills = {"Eclipse", "IntelliJ IDEA", "Spring Tool Suite", "Postman", "JIRA", "Git & GitHub", "GitHub Copilot"};
        for (String skill : toolsSkills) {
            Skill s = new Skill();
            s.setSkillName(skill);
            s.setCategory("Tools");
            s.setLevel(85);
            skillRepository.save(s);
        }

        String[] cloudSkills = {"AWS S3", "AWS EC2", "Docker", "Kafka", "RabbitMQ"};
        for (String skill : cloudSkills) {
            Skill s = new Skill();
            s.setSkillName(skill);
            s.setCategory("Cloud & DevOps");
            s.setLevel(80);
            skillRepository.save(s);
        }

        Project project = new Project();
        project.setTitle("Travel & Hospitality Platform");
        project.setDescription("A scalable and high-performance online hotel booking platform enabling users to search, compare, book, and review hotels seamlessly. Integrates external APIs for real-time availability and pricing, supports secure authentication, robust property management, and automated notifications.");
        project.setTechStack("Java, Spring Boot, Hibernate/JPA, MySQL, JWT, Spring Security, RESTful APIs, Microservices, Angular, TypeScript, AWS S3");
        project.setGithubLink("https://github.com/himanshusingh/hotel-booking");
        projectRepository.save(project);

        Resume resume = new Resume();
        resume.setFileName("Himanshu_Resume.pdf");
        resume.setUpdatedAt(LocalDateTime.now());
        resumeRepository.save(resume);
    }




}
