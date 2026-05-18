package com.campusconnect.controller;

import com.campusconnect.dto.CommentDTO;
import com.campusconnect.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
//@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody Map<String, Object> request) {
        Long postId = Long.valueOf(request.get("postId").toString());
        Long userId = Long.valueOf(request.get("userId").toString());
        String content = request.get("content").toString();

        return ResponseEntity.ok(commentService.createComment(postId, userId, content));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }
}