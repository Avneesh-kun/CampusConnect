package com.campusconnect.controller;

import com.campusconnect.model.VerificationRequest;
import com.campusconnect.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/verification")
//@CrossOrigin(origins = "http://localhost:3000")
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @GetMapping("/pending")
    public ResponseEntity<List<VerificationRequest>> getPendingRequests() {
        return ResponseEntity.ok(verificationService.getPendingRequests());
    }

    @GetMapping("/all")
    public ResponseEntity<List<VerificationRequest>> getAllRequests() {
        return ResponseEntity.ok(verificationService.getAllRequests());
    }

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<?> approveUser(
            @PathVariable Long requestId,
            @RequestBody Map<String, Object> payload) {
        try {
            Long adminId = Long.valueOf(payload.get("adminId").toString());
            String notes = payload.getOrDefault("notes", "").toString();

            VerificationRequest updated = verificationService.approveUser(requestId, adminId, notes);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<?> rejectUser(
            @PathVariable Long requestId,
            @RequestBody Map<String, Object> payload) {
        try {
            Long adminId = Long.valueOf(payload.get("adminId").toString());
            String reason = payload.getOrDefault("reason", "No reason provided").toString();

            VerificationRequest updated = verificationService.rejectUser(requestId, adminId, reason);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}