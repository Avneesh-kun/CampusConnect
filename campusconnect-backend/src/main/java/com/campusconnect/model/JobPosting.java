package com.campusconnect.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_postings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "posted_by_id", nullable = false)
    private User postedBy; // Alumni who posted

    @Column(nullable = false)
    private String title;

    @Column(length = 3000)
    private String description;

    @Column(nullable = false)
    private String companyName;

    private String companyLogo;

    private String location;

    @Enumerated(EnumType.STRING)
    private JobType jobType; // FULL_TIME, PART_TIME, INTERNSHIP, CONTRACT

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel; // ENTRY, INTERMEDIATE, SENIOR

    private String salaryRange;

    @Column(length = 1000)
    private String requirements;

    @Column(length = 1000)
    private String responsibilities;

    private String applyLink;
    private String applyEmail;

    private LocalDateTime applicationDeadline;

    private Integer applicationsCount = 0;

    private boolean isActive = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}