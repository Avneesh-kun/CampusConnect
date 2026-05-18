package com.campusconnect.controller;

import com.campusconnect.model.*;
import com.campusconnect.repository.UserRepository;
import com.campusconnect.service.VerificationService;
import com.campusconnect.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            System.out.println("=== REGISTRATION ===");
            System.out.println("Email: " + user.getEmail());

            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email already registered"));
            }

            // IMPORTANT: Set status to PENDING
            user.setAccountStatus(AccountStatus.PENDING);
            System.out.println("Setting account status to: PENDING");

            User savedUser = userRepository.save(user);
            System.out.println("User saved with ID: " + savedUser.getId());
            System.out.println("Account status: " + savedUser.getAccountStatus());

            // Create verification request
            VerificationRequest request = verificationService.createVerificationRequest(savedUser);
            System.out.println("Verification request created with ID: " + request.getId());

            // Notify admins
            notificationService.notifyAdminsNewRegistration(savedUser);
            System.out.println("Admin notified");

            return ResponseEntity.ok(Map.of(
                    "message", "Registration successful! Your account is pending admin verification.",
                    "status", "PENDING"
            ));

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid email or password"));
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(password)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid email or password"));
        }

        if (user.getAccountStatus() == AccountStatus.PENDING) {
            return ResponseEntity.status(403)
                    .body(Map.of(
                            "error", "Your account is pending admin verification. Please wait for approval.",
                            "status", "PENDING"
                    ));
        }

        if (user.getAccountStatus() == AccountStatus.REJECTED) {
            String reason = user.getRejectionReason() != null ?
                    user.getRejectionReason() : "No reason provided";
            return ResponseEntity.status(403)
                    .body(Map.of(
                            "error", "Your account has been rejected. Reason: " + reason,
                            "status", "REJECTED"
                    ));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        response.put("role", user.getRole().toString());
        response.put("accountStatus", user.getAccountStatus().toString());
        response.put("department", user.getDepartment());
        response.put("rollNumber", user.getRollNumber());

        return ResponseEntity.ok(response);
    }
}