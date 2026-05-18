package com.campusconnect.repository;

import com.campusconnect.model.AccountStatus;
import com.campusconnect.model.VerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, Long> {
    List<VerificationRequest> findByStatusOrderByRequestedAtDesc(AccountStatus status);
    List<VerificationRequest> findAllByOrderByRequestedAtDesc();
    Optional<VerificationRequest> findByUserId(Long userId);
}