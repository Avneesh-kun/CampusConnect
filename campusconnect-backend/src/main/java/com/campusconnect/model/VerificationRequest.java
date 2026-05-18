package com.campusconnect.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "reviewed_by_id")
    private User reviewedBy;

    private String reviewNotes;

    @Column(updatable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime reviewedAt;

    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();
    }
}