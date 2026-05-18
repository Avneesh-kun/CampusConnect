package com.campusconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FollowRequestDTO {
    private Long id;
    private UserDTO follower;
    private UserDTO following;
    private String status;
    private LocalDateTime createdAt;
}