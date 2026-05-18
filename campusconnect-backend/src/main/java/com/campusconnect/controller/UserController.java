package com.campusconnect.controller;

import com.campusconnect.dto.LoginRequest;
import com.campusconnect.dto.UserDTO;
import com.campusconnect.model.User;
import com.campusconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(userService.searchUsers(search));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            UserDTO createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserDTO user = userService.login(loginRequest);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserProfile(
            @PathVariable Long id,
            @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUserProfile(id, user));
    }
}