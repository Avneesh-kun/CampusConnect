package com.campusconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostDTO {
    private Long id;
    private UserDTO user;
    private String content;
    private String imageUrl;
    private Integer likesCount;
    private Integer commentsCount;
    private LocalDateTime createdAt;
    private boolean liked; // NEW: Is this post liked by current user?
}