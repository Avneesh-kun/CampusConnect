package com.campusconnect.controller;

import com.campusconnect.dto.PostDTO;
import com.campusconnect.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
//@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long profileUserId) {
        if (profileUserId != null) {
            // Get posts by specific user
            return ResponseEntity.ok(postService.getPostsByUser(profileUserId, userId));
        }
        if (userId != null) {
            return ResponseEntity.ok(postService.getAllPostsForUser(userId));
        }
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String content = request.get("content").toString();
            String imageUrl = request.getOrDefault("imageUrl", "").toString();

            return ResponseEntity.ok(postService.createPost(userId, content, imageUrl));
        } catch (Exception e) {
            System.err.println("Error creating post: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostDTO> likePost(
            @PathVariable Long postId,
            @RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        return ResponseEntity.ok(postService.likePost(postId, userId));
    }

    @PostMapping("/{postId}/unlike")
    public ResponseEntity<PostDTO> unlikePost(
            @PathVariable Long postId,
            @RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        return ResponseEntity.ok(postService.unlikePost(postId, userId));
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        try {
            postService.deletePost(postId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }
    }
    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String content = request.get("content").toString();
            String imageUrl = request.getOrDefault("imageUrl", null) != null
                    ? request.get("imageUrl").toString()
                    : null;

            return ResponseEntity.ok(postService.updatePost(postId, userId, content, imageUrl));
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }
    }

}