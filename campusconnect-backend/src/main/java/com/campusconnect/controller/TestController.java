package com.campusconnect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:3000")
public class TestController {

    @GetMapping("/health")
    public String healthCheck() {
        return "CampusConnect Backend is running!";
    }
}
