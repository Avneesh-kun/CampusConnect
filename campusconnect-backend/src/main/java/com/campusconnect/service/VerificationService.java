package com.campusconnect.service;

import com.campusconnect.model.*;
import com.campusconnect.repository.UserRepository;
import com.campusconnect.repository.VerificationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationService {

    @Autowired
    private VerificationRequestRepository verificationRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public VerificationRequest createVerificationRequest(User user) {
        VerificationRequest request = new VerificationRequest();
        request.setUser(user);
        request.setStatus(AccountStatus.PENDING);
        return verificationRequestRepository.save(request);
    }

    public List<VerificationRequest> getPendingRequests() {
        return verificationRequestRepository.findByStatusOrderByRequestedAtDesc(AccountStatus.PENDING);
    }

    public List<VerificationRequest> getAllRequests() {
        return verificationRequestRepository.findAllByOrderByRequestedAtDesc();
    }

    @Transactional
    public VerificationRequest approveUser(Long requestId, Long adminId, String notes) {
        VerificationRequest request = verificationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Verification request not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!admin.getRole().equals(UserRole.ADMIN)) {
            throw new RuntimeException("Only admins can verify accounts");
        }

        User user = request.getUser();
        user.setAccountStatus(AccountStatus.APPROVED);
        user.setVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        request.setStatus(AccountStatus.APPROVED);
        request.setReviewedBy(admin);
        request.setReviewNotes(notes);
        request.setReviewedAt(LocalDateTime.now());
        VerificationRequest updated = verificationRequestRepository.save(request);

        notificationService.createAccountVerificationNotification(user, true, notes);

        return updated;
    }

    @Transactional
    public VerificationRequest rejectUser(Long requestId, Long adminId, String reason) {
        VerificationRequest request = verificationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Verification request not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!admin.getRole().equals(UserRole.ADMIN)) {
            throw new RuntimeException("Only admins can verify accounts");
        }

        User user = request.getUser();
        user.setAccountStatus(AccountStatus.REJECTED);
        user.setRejectionReason(reason);
        userRepository.save(user);

        request.setStatus(AccountStatus.REJECTED);
        request.setReviewedBy(admin);
        request.setReviewNotes(reason);
        request.setReviewedAt(LocalDateTime.now());
        VerificationRequest updated = verificationRequestRepository.save(request);

        notificationService.createAccountVerificationNotification(user, false, reason);

        return updated;
    }
}