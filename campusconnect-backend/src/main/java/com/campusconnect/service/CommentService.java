package com.campusconnect.service;

import com.campusconnect.dto.CommentDTO;
import com.campusconnect.dto.UserDTO;
import com.campusconnect.model.Comment;
import com.campusconnect.model.Post;
import com.campusconnect.model.User;
import com.campusconnect.repository.CommentRepository;
import com.campusconnect.repository.PostRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public List<CommentDTO> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return commentRepository.findByPostOrderByCreatedAtDesc(post).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO createComment(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);

        Comment savedComment = commentRepository.save(comment);

        // Update post comment count
        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);

        // Create notification
        notificationService.createCommentNotification(post.getUser(), comment.getUser(), post);

        return convertToDTO(savedComment);
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Check if user owns the comment
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this comment");
        }

        Post post = comment.getPost();
        commentRepository.delete(comment);

        // Update post comment count
        post.setCommentsCount(Math.max(0, post.getCommentsCount() - 1));
        postRepository.save(post);
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(comment.getUser().getId());
        userDTO.setFirstName(comment.getUser().getFirstName());
        userDTO.setLastName(comment.getUser().getLastName());
        userDTO.setProfilePicture(comment.getUser().getProfilePicture());
        dto.setUser(userDTO);

        return dto;
    }
}