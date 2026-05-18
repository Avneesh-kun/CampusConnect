package com.campusconnect.service;

import com.campusconnect.dto.JobPostingDTO;
import com.campusconnect.dto.JobApplicationDTO;
import com.campusconnect.dto.UserDTO;
import com.campusconnect.model.*;
import com.campusconnect.repository.JobApplicationRepository;
import com.campusconnect.repository.JobPostingRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public List<JobPostingDTO> getAllActiveJobs(Long userId) {
        return jobPostingRepository.findByIsActiveTrueOrderByCreatedAtDesc().stream()
                .map(job -> convertToDTO(job, userId))
                .collect(Collectors.toList());
    }

    public List<JobPostingDTO> getJobsByType(JobType jobType, Long userId) {
        return jobPostingRepository.findByJobTypeAndIsActiveTrueOrderByCreatedAtDesc(jobType).stream()
                .map(job -> convertToDTO(job, userId))
                .collect(Collectors.toList());
    }

    public List<JobPostingDTO> getMyPostedJobs(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jobPostingRepository.findByPostedByOrderByCreatedAtDesc(user).stream()
                .map(job -> convertToDTO(job, userId))
                .collect(Collectors.toList());
    }

    public JobPostingDTO createJobPosting(Long userId, JobPosting jobPosting) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only alumni can post jobs
        if (!user.getRole().equals(UserRole.ALUMNI)) {
            throw new RuntimeException("Only alumni can post job opportunities");
        }

        jobPosting.setPostedBy(user);
        JobPosting savedJob = jobPostingRepository.save(jobPosting);

        return convertToDTO(savedJob, userId);
    }

    public JobPostingDTO updateJobPosting(Long jobId, Long userId, JobPosting updatedJob) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job posting not found"));

        if (!job.getPostedBy().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to edit this job posting");
        }

        job.setTitle(updatedJob.getTitle());
        job.setDescription(updatedJob.getDescription());
        job.setCompanyName(updatedJob.getCompanyName());
        job.setCompanyLogo(updatedJob.getCompanyLogo());
        job.setLocation(updatedJob.getLocation());
        job.setJobType(updatedJob.getJobType());
        job.setExperienceLevel(updatedJob.getExperienceLevel());
        job.setSalaryRange(updatedJob.getSalaryRange());
        job.setRequirements(updatedJob.getRequirements());
        job.setResponsibilities(updatedJob.getResponsibilities());
        job.setApplyLink(updatedJob.getApplyLink());
        job.setApplyEmail(updatedJob.getApplyEmail());
        job.setApplicationDeadline(updatedJob.getApplicationDeadline());

        JobPosting savedJob = jobPostingRepository.save(job);
        return convertToDTO(savedJob, userId);
    }

    public void deleteJobPosting(Long jobId, Long userId) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job posting not found"));

        if (!job.getPostedBy().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this job posting");
        }

        jobPostingRepository.delete(job);
    }

    public JobApplicationDTO applyForJob(Long jobId, Long userId, String coverLetter, String resumeUrl) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job posting not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if already applied
        if (jobApplicationRepository.existsByUserAndJobPosting(user, job)) {
            throw new RuntimeException("Already applied for this job");
        }

        // Check if deadline passed
        if (job.getApplicationDeadline() != null &&
                LocalDateTime.now().isAfter(job.getApplicationDeadline())) {
            throw new RuntimeException("Application deadline has passed");
        }

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJobPosting(job);
        application.setCoverLetter(coverLetter);
        application.setResumeUrl(resumeUrl);
        application.setStatus(ApplicationStatus.PENDING);

        JobApplication savedApplication = jobApplicationRepository.save(application);

        // Update application count
        job.setApplicationsCount(job.getApplicationsCount() + 1);
        jobPostingRepository.save(job);

        // Notify job poster
        notificationService.createJobApplicationNotification(job.getPostedBy(), user, job);

        return convertApplicationToDTO(savedApplication);
    }

    public List<JobApplicationDTO> getJobApplications(Long jobId, Long userId) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job posting not found"));

        if (!job.getPostedBy().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to view applications");
        }

        return jobApplicationRepository.findByJobPosting(job).stream()
                .map(this::convertApplicationToDTO)
                .collect(Collectors.toList());
    }

    public List<JobApplicationDTO> getMyApplications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jobApplicationRepository.findByUserOrderByAppliedAtDesc(user).stream()
                .map(this::convertApplicationToDTO)
                .collect(Collectors.toList());
    }

    public JobApplicationDTO updateApplicationStatus(Long applicationId, Long userId, ApplicationStatus status) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getJobPosting().getPostedBy().getId().equals(userId)) {
            throw new RuntimeException("Not authorized");
        }

        application.setStatus(status);
        JobApplication updated = jobApplicationRepository.save(application);

        // Notify applicant
        notificationService.createApplicationStatusNotification(
                application.getUser(),
                application.getJobPosting(),
                status
        );

        return convertApplicationToDTO(updated);
    }

    private JobPostingDTO convertToDTO(JobPosting job, Long userId) {
        JobPostingDTO dto = new JobPostingDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setCompanyName(job.getCompanyName());
        dto.setCompanyLogo(job.getCompanyLogo());
        dto.setLocation(job.getLocation());
        dto.setJobType(job.getJobType() != null ? job.getJobType().toString() : null);
        dto.setExperienceLevel(job.getExperienceLevel() != null ? job.getExperienceLevel().toString() : null);
        dto.setSalaryRange(job.getSalaryRange());
        dto.setRequirements(job.getRequirements());
        dto.setResponsibilities(job.getResponsibilities());
        dto.setApplyLink(job.getApplyLink());
        dto.setApplyEmail(job.getApplyEmail());
        dto.setApplicationDeadline(job.getApplicationDeadline());
        dto.setApplicationsCount(job.getApplicationsCount());
        dto.setActive(job.isActive());
        dto.setCreatedAt(job.getCreatedAt());

        UserDTO postedByDTO = new UserDTO();
        postedByDTO.setId(job.getPostedBy().getId());
        postedByDTO.setFirstName(job.getPostedBy().getFirstName());
        postedByDTO.setLastName(job.getPostedBy().getLastName());
        postedByDTO.setEmail(job.getPostedBy().getEmail());
        dto.setPostedBy(postedByDTO);

        // Check if current user has applied
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                dto.setHasApplied(jobApplicationRepository.existsByUserAndJobPosting(user, job));
            }
        }

        return dto;
    }

    private JobApplicationDTO convertApplicationToDTO(JobApplication application) {
        JobApplicationDTO dto = new JobApplicationDTO();
        dto.setId(application.getId());
        dto.setCoverLetter(application.getCoverLetter());
        dto.setResumeUrl(application.getResumeUrl());
        dto.setStatus(application.getStatus().toString());
        dto.setAppliedAt(application.getAppliedAt());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(application.getUser().getId());
        userDTO.setFirstName(application.getUser().getFirstName());
        userDTO.setLastName(application.getUser().getLastName());
        userDTO.setEmail(application.getUser().getEmail());
        userDTO.setRollNumber(application.getUser().getRollNumber());
        userDTO.setDepartment(application.getUser().getDepartment());
        dto.setUser(userDTO);

        dto.setJobPosting(convertToDTO(application.getJobPosting(), null));

        return dto;
    }
}