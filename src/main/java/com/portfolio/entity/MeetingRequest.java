package com.portfolio.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "meeting_request")
@Data
public class MeetingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private LocalDateTime preferredDatetime;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private String status = "PENDING";

    private String meetingLink;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
