package com.campusconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private UserDTO actor; // Who performed the action
    private String type; // LIKE or COMMENT
    private Long postId;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}