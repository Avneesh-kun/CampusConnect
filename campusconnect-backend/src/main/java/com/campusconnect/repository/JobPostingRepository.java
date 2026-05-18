package com.campusconnect.repository;

import com.campusconnect.model.JobPosting;
import com.campusconnect.model.JobType;
import com.campusconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findAllByOrderByCreatedAtDesc();
    List<JobPosting> findByPostedByOrderByCreatedAtDesc(User postedBy);
    List<JobPosting> findByIsActiveTrueOrderByCreatedAtDesc();
    List<JobPosting> findByJobTypeAndIsActiveTrueOrderByCreatedAtDesc(JobType jobType);
    List<JobPosting> findByApplicationDeadlineAfterAndIsActiveTrueOrderByCreatedAtDesc(LocalDateTime date);
}