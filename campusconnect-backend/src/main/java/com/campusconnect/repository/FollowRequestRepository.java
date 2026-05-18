package com.campusconnect.repository;

import com.campusconnect.model.FollowRequest;
import com.campusconnect.model.RequestStatus;
import com.campusconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
    Optional<FollowRequest> findByFollowerAndFollowing(User follower, User following);
    List<FollowRequest> findByFollowingAndStatus(User following, RequestStatus status);
    List<FollowRequest> findByFollowerAndStatus(User follower, RequestStatus status);
    long countByFollowingAndStatus(User following, RequestStatus status);
    boolean existsByFollowerAndFollowingAndStatus(User follower, User following, RequestStatus status);
}