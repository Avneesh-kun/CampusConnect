package com.campusconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobPostingDTO {
    private Long id;
    private UserDTO postedBy;
    private String title;
    private String description;
    private String companyName;
    private String companyLogo;
    private String location;
    private String jobType;
    private String experienceLevel;
    private String salaryRange;
    private String requirements;
    private String responsibilities;
    private String applyLink;
    private String applyEmail;
    private LocalDateTime applicationDeadline;
    private Integer applicationsCount;
    private boolean isActive;
    private LocalDateTime createdAt;
    private boolean hasApplied; // Current user has applied
}