package com.campusconnect.repository;

import com.campusconnect.model.JobApplication;
import com.campusconnect.model.JobPosting;
import com.campusconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    Optional<JobApplication> findByUserAndJobPosting(User user, JobPosting jobPosting);
    List<JobApplication> findByJobPosting(JobPosting jobPosting);
    List<JobApplication> findByUserOrderByAppliedAtDesc(User user);
    boolean existsByUserAndJobPosting(User user, JobPosting jobPosting);
    long countByJobPosting(JobPosting jobPosting);
}