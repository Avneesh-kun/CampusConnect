package com.campusconnect.service;

import com.campusconnect.dto.PostDTO;
import com.campusconnect.dto.UserDTO;
import com.campusconnect.model.Like;
import com.campusconnect.model.Post;
import com.campusconnect.model.User;
import com.campusconnect.repository.LikeRepository;
import com.campusconnect.repository.PostRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private NotificationService notificationService;

    public List<PostDTO> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getAllPostsForUser(Long userId) {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(post -> convertToDTOWithLikeStatus(post, userId))
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByUser(Long userId, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return postRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(post -> convertToDTOWithLikeStatus(post, currentUserId))
                .collect(Collectors.toList());
    }

    public PostDTO createPost(Long userId, String content, String imageUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setImageUrl(imageUrl);

        Post savedPost = postRepository.save(post);
        return convertToDTO(savedPost);
    }

    public PostDTO likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if already liked
        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new RuntimeException("Post already liked");
        }

        // Create like
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

        // Update post likes count
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);

        // Create notification
        notificationService.createLikeNotification(post.getUser(), user, post);

        return convertToDTOWithLikeStatus(post, userId);
    }

    public PostDTO unlikePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find and delete like
        Like like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        likeRepository.delete(like);

        // Update post likes count
        post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
        postRepository.save(post);

        return convertToDTOWithLikeStatus(post, userId);
    }
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Check if user owns the post
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this post");
        }

        // Delete associated likes and comments first (cascade should handle this, but being explicit)
        likeRepository.deleteAll(likeRepository.findByPost(post));

        // Delete the post
        postRepository.delete(post);
    }
    public PostDTO updatePost(Long postId, Long userId, String content, String imageUrl) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Check if user owns the post
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to edit this post");
        }

        post.setContent(content);
        if (imageUrl != null) {
            post.setImageUrl(imageUrl);
        }

        Post updatedPost = postRepository.save(post);
        return convertToDTOWithLikeStatus(updatedPost, userId);
    }




    private PostDTO convertToDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setLikesCount(post.getLikesCount());
        dto.setCommentsCount(post.getCommentsCount());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setLiked(false); // Default

        UserDTO userDTO = new UserDTO();
        userDTO.setId(post.getUser().getId());
        userDTO.setFirstName(post.getUser().getFirstName());
        userDTO.setLastName(post.getUser().getLastName());
        userDTO.setProfilePicture(post.getUser().getProfilePicture());
        dto.setUser(userDTO);

        return dto;
    }

    private PostDTO convertToDTOWithLikeStatus(Post post, Long userId) {
        PostDTO dto = convertToDTO(post);

        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                dto.setLiked(likeRepository.existsByUserAndPost(user, post));
            }
        }

        return dto;
    }
}