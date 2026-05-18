package com.campusconnect.controller;

import com.campusconnect.dto.JobApplicationDTO;
import com.campusconnect.dto.JobPostingDTO;
import com.campusconnect.model.ApplicationStatus;
import com.campusconnect.model.JobPosting;
import com.campusconnect.model.JobType;
import com.campusconnect.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
//@CrossOrigin(origins = "http://localhost:3000")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @GetMapping
    public ResponseEntity<List<JobPostingDTO>> getAllActiveJobs(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(jobPostingService.getAllActiveJobs(userId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<JobPostingDTO>> getJobsByType(
            @PathVariable String type,
            @RequestParam(required = false) Long userId) {
        JobType jobType = JobType.valueOf(type.toUpperCase());
        return ResponseEntity.ok(jobPostingService.getJobsByType(jobType, userId));
    }

    @GetMapping("/my-posts/{userId}")
    public ResponseEntity<List<JobPostingDTO>> getMyPostedJobs(@PathVariable Long userId) {
        return ResponseEntity.ok(jobPostingService.getMyPostedJobs(userId));
    }

    @PostMapping
    public ResponseEntity<?> createJobPosting(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());

            JobPosting job = new JobPosting();
            job.setTitle(request.get("title").toString());
            job.setDescription(request.get("description").toString());
            job.setCompanyName(request.get("companyName").toString());
            job.setLocation(request.get("location").toString());
            job.setJobType(JobType.valueOf(request.get("jobType").toString()));

            if (request.containsKey("companyLogo")) {
                job.setCompanyLogo(request.get("companyLogo").toString());
            }
            if (request.containsKey("experienceLevel")) {
                job.setExperienceLevel(
                        com.campusconnect.model.ExperienceLevel.valueOf(
                                request.get("experienceLevel").toString()
                        )
                );
            }
            if (request.containsKey("salaryRange")) {
                job.setSalaryRange(request.get("salaryRange").toString());
            }
            if (request.containsKey("requirements")) {
                job.setRequirements(request.get("requirements").toString());
            }
            if (request.containsKey("responsibilities")) {
                job.setResponsibilities(request.get("responsibilities").toString());
            }
            if (request.containsKey("applyLink")) {
                job.setApplyLink(request.get("applyLink").toString());
            }
            if (request.containsKey("applyEmail")) {
                job.setApplyEmail(request.get("applyEmail").toString());
            }
            if (request.containsKey("applicationDeadline")) {
                job.setApplicationDeadline(
                        LocalDateTime.parse(request.get("applicationDeadline").toString())
                );
            }

            return ResponseEntity.ok(jobPostingService.createJobPosting(userId, job));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<?> updateJobPosting(
            @PathVariable Long jobId,
            @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());

            JobPosting job = new JobPosting();
            job.setTitle(request.get("title").toString());
            job.setDescription(request.get("description").toString());
            job.setCompanyName(request.get("companyName").toString());
            job.setLocation(request.get("location").toString());
            job.setJobType(JobType.valueOf(request.get("jobType").toString()));

            if (request.containsKey("companyLogo")) {
                job.setCompanyLogo(request.get("companyLogo").toString());
            }
            if (request.containsKey("experienceLevel")) {
                job.setExperienceLevel(
                        com.campusconnect.model.ExperienceLevel.valueOf(
                                request.get("experienceLevel").toString()
                        )
                );
            }
            if (request.containsKey("salaryRange")) {
                job.setSalaryRange(request.get("salaryRange").toString());
            }
            if (request.containsKey("requirements")) {
                job.setRequirements(request.get("requirements").toString());
            }
            if (request.containsKey("responsibilities")) {
                job.setResponsibilities(request.get("responsibilities").toString());
            }
            if (request.containsKey("applyLink")) {
                job.setApplyLink(request.get("applyLink").toString());
            }
            if (request.containsKey("applyEmail")) {
                job.setApplyEmail(request.get("applyEmail").toString());
            }
            if (request.containsKey("applicationDeadline")) {
                job.setApplicationDeadline(
                        LocalDateTime.parse(request.get("applicationDeadline").toString())
                );
            }

            return ResponseEntity.ok(jobPostingService.updateJobPosting(jobId, userId, job));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> deleteJobPosting(
            @PathVariable Long jobId,
            @RequestParam Long userId) {
        try {
            jobPostingService.deleteJobPosting(jobId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{jobId}/apply")
    public ResponseEntity<?> applyForJob(
            @PathVariable Long jobId,
            @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String coverLetter = request.get("coverLetter").toString();
            String resumeUrl = request.getOrDefault("resumeUrl", "").toString();

            return ResponseEntity.ok(jobPostingService.applyForJob(jobId, userId, coverLetter, resumeUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{jobId}/applications")
    public ResponseEntity<List<JobApplicationDTO>> getJobApplications(
            @PathVariable Long jobId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(jobPostingService.getJobApplications(jobId, userId));
    }

    @GetMapping("/my-applications/{userId}")
    public ResponseEntity<List<JobApplicationDTO>> getMyApplications(@PathVariable Long userId) {
        return ResponseEntity.ok(jobPostingService.getMyApplications(userId));
    }

    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            ApplicationStatus status = ApplicationStatus.valueOf(request.get("status").toString());

            return ResponseEntity.ok(jobPostingService.updateApplicationStatus(applicationId, userId, status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}