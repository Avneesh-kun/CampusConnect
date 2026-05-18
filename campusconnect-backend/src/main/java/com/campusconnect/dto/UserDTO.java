package com.campusconnect.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String rollNumber;
    private String role;
    private String department;
    private String course;
    private Integer academicYear;
    private Integer batchYear;
    private String profilePicture;
    private String bio;
}