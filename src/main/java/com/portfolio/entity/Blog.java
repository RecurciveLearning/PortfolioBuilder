package com.portfolio.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {
    @Index(name = "idx_blog_created_at", columnList = "createdAt"),
    @Index(name = "idx_blog_category", columnList = "category_id")
})
public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String content;

    // store image URL or path
    private String image;

    private LocalDateTime createdAt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id")
    private Category category;

    public Blog() {}

    // convenience constructor
    public Blog(String title, String content, String image, Category category) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.category = category;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    // Derived helper: summary for listing
    @Transient
    public String getSummary() {
        if (content == null) return "";
        int limit = 120;
        return content.length() <= limit ? content : content.substring(0, limit).trim() + "...";
    }

    // helper used by Thymeleaf template (keeps your template as-is)
    @Transient
    public String getImageUrl() {
        return this.image;
    }
}
