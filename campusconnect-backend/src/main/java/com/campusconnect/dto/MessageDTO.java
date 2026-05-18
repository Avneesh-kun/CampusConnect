package com.campusconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private UserDTO sender;
    private UserDTO receiver;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;
}