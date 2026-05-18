package com.campusconnect.service;

import com.campusconnect.dto.LoginRequest;
import com.campusconnect.dto.UserDTO;
import com.campusconnect.model.User;
import com.campusconnect.repository.UserRepository;
import com.campusconnect.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    public UserDTO createUser(User user) {
        try {
            System.out.println("Creating user: " + user.getEmail());

            if (userRepository.existsByEmail(user.getEmail())) {
                throw new RuntimeException("User with this email already exists");
            }

            // Validate fields based on role
            validateUserFields(user);

            User savedUser = userRepository.save(user);
            System.out.println("User saved with ID: " + savedUser.getId());

            return convertToDTO(savedUser);
        } catch (Exception e) {
            System.err.println("Error in createUser: " + e.getMessage());
            throw e;
        }
    }

    public UserDTO login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Check password (in production, use BCrypt or similar)
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return convertToDTO(user);
    }

    public Map<String, Object> getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> profile = new HashMap<>();
        profile.put("user", convertToDTO(user));

        long postCount = postRepository.findByUserOrderByCreatedAtDesc(user).size();
        profile.put("postCount", postCount);

        return profile;
    }

    public UserDTO updateUserProfile(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setBio(updatedUser.getBio());
        user.setDepartment(updatedUser.getDepartment());

        // Update role-specific fields
        if (user.getRole().toString().equals("STUDENT")) {
            user.setCourse(updatedUser.getCourse());
            user.setAcademicYear(updatedUser.getAcademicYear());
        } else if (user.getRole().toString().equals("ALUMNI")) {
            user.setBatchYear(updatedUser.getBatchYear());
        }

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public List<UserDTO> searchUsers(String query) {
        String searchQuery = query.toLowerCase();
        return userRepository.findAll().stream()
                .filter(user ->
                        user.getFirstName().toLowerCase().contains(searchQuery) ||
                                user.getLastName().toLowerCase().contains(searchQuery) ||
                                user.getEmail().toLowerCase().contains(searchQuery) ||
                                (user.getDepartment() != null && user.getDepartment().toLowerCase().contains(searchQuery)) ||
                                (user.getCourse() != null && user.getCourse().toLowerCase().contains(searchQuery))
                )
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void validateUserFields(User user) {
        String role = user.getRole().toString();

        switch (role) {
            case "STUDENT":
                if (user.getRollNumber() == null || user.getRollNumber().isEmpty()) {
                    throw new RuntimeException("Roll number is required for students");
                }
                break;
            case "ALUMNI":
                if (user.getBatchYear() == null) {
                    throw new RuntimeException("Batch year is required for alumni");
                }
                // Clear student-specific fields
                user.setRollNumber(null);
                user.setCourse(null);
                user.setAcademicYear(null);
                break;
            case "FACULTY":
            case "ADMIN":
                // Clear student-specific fields
                user.setRollNumber(null);
                user.setCourse(null);
                user.setAcademicYear(null);
                user.setBatchYear(null);
                break;
        }
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRollNumber(user.getRollNumber());
        dto.setRole(user.getRole() != null ? user.getRole().toString() : null);
        dto.setDepartment(user.getDepartment());
        dto.setCourse(user.getCourse());
        dto.setAcademicYear(user.getAcademicYear());
        dto.setBatchYear(user.getBatchYear());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setBio(user.getBio());
        return dto;
    }
}