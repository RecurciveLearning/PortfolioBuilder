package com.portfolio.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "education")
@Data
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String degree;

    @Column(nullable = false)
    private String university;

    @Column(nullable = false)
    private String year;
}
