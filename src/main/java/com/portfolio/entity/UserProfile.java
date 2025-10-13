package com.portfolio.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_profile")
@Data
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String aboutMe;

    @Column(nullable = false)
    private String email;

    private String phone;
    private String city;
    private String linkedin;
    private String github;
    private String profileImage;
}
