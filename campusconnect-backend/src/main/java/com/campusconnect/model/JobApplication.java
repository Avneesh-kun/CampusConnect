package com.campusconnect.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "job_posting_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Student applying

    @ManyToOne
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @Column(length = 2000)
    private String coverLetter;

    private String resumeUrl;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // PENDING, REVIEWED, SHORTLISTED, REJECTED

    @Column(updatable = false)
    private LocalDateTime appliedAt;

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
    }
}