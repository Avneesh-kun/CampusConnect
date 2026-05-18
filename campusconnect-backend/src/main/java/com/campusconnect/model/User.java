package com.campusconnect.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String rollNumber; // Only for STUDENT

    @Enumerated(EnumType.STRING)
    private UserRole role; // STUDENT, FACULTY, ADMIN, ALUMNI

    private String department;
    private String course; // Only for STUDENT
    private Integer academicYear; // Only for STUDENT
    private Integer batchYear; // Only for ALUMNI

    private String profilePicture;
    private String bio;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Add these fields to existing User entity
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus = AccountStatus.PENDING;

    private String rejectionReason;
    private LocalDateTime verifiedAt;

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