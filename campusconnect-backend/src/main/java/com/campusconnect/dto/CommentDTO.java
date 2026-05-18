package com.campusconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private UserDTO user;
    private String content;
    private LocalDateTime createdAt;
}