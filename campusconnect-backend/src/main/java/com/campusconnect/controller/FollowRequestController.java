package com.campusconnect.controller;

import com.campusconnect.dto.FollowRequestDTO;
import com.campusconnect.service.FollowRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follow-requests")
//@CrossOrigin(origins = "http://localhost:3000")
public class FollowRequestController {

    @Autowired
    private FollowRequestService followRequestService;

    @PostMapping("/send")
    public ResponseEntity<?> sendFollowRequest(@RequestBody Map<String, Long> request) {
        try {
            Long followerId = request.get("followerId");
            Long followingId = request.get("followingId");
            return ResponseEntity.ok(followRequestService.sendFollowRequest(followerId, followingId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{requestId}/accept")
    public ResponseEntity<?> acceptFollowRequest(@PathVariable Long requestId) {
        try {
            return ResponseEntity.ok(followRequestService.acceptFollowRequest(requestId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<?> rejectFollowRequest(@PathVariable Long requestId) {
        try {
            return ResponseEntity.ok(followRequestService.rejectFollowRequest(requestId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<?> unfollowUser(@RequestBody Map<String, Long> request) {
        try {
            Long followerId = request.get("followerId");
            Long followingId = request.get("followingId");
            followRequestService.unfollowUser(followerId, followingId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/pending/{userId}")
    public ResponseEntity<List<FollowRequestDTO>> getPendingRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(followRequestService.getPendingRequests(userId));
    }

    @GetMapping("/pending-count/{userId}")
    public ResponseEntity<Long> getPendingRequestCount(@PathVariable Long userId) {
        return ResponseEntity.ok(followRequestService.getPendingRequestCount(userId));
    }

    @GetMapping("/status")
    public ResponseEntity<String> getFollowStatus(
            @RequestParam Long userId,
            @RequestParam Long targetUserId) {
        return ResponseEntity.ok(followRequestService.getFollowStatus(userId, targetUserId));
    }
}