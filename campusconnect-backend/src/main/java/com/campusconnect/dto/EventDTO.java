package com.campusconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private UserDTO organizer;
    private String location;
    private LocalDateTime eventDate;
    private LocalDateTime eventEndDate;
    private String category;
    private String imageUrl;
    private Integer attendeesCount;
    private LocalDateTime createdAt;
    private boolean isAttending; // Is current user attending?
}