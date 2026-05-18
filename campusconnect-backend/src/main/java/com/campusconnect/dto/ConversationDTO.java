package com.campusconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConversationDTO {
    private Long id;
    private UserDTO otherUser;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Integer unreadCount;
}