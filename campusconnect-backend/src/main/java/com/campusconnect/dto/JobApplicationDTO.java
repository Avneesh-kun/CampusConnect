package com.campusconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobApplicationDTO {
    private Long id;
    private UserDTO user;
    private JobPostingDTO jobPosting;
    private String coverLetter;
    private String resumeUrl;
    private String status;
    private LocalDateTime appliedAt;
}