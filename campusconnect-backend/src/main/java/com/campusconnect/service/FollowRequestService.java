package com.campusconnect.service;

import com.campusconnect.dto.FollowRequestDTO;
import com.campusconnect.dto.UserDTO;
import com.campusconnect.model.FollowRequest;
import com.campusconnect.model.RequestStatus;
import com.campusconnect.model.User;
import com.campusconnect.repository.FollowRequestRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowRequestService {

    @Autowired
    private FollowRequestRepository followRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public FollowRequestDTO sendFollowRequest(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if request already exists
        if (followRequestRepository.existsByFollowerAndFollowingAndStatus(
                follower, following, RequestStatus.PENDING)) {
            throw new RuntimeException("Follow request already sent");
        }

        // Check if already accepted
        if (followRequestRepository.existsByFollowerAndFollowingAndStatus(
                follower, following, RequestStatus.ACCEPTED)) {
            throw new RuntimeException("Already following");
        }

        FollowRequest request = new FollowRequest();
        request.setFollower(follower);
        request.setFollowing(following);
        request.setStatus(RequestStatus.PENDING);

        FollowRequest savedRequest = followRequestRepository.save(request);

        // Create notification for the user being followed
        notificationService.createFollowRequestNotification(following, follower);

        return convertToDTO(savedRequest);
    }

    public FollowRequestDTO acceptFollowRequest(Long requestId) {
        FollowRequest request = followRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request already processed");
        }

        request.setStatus(RequestStatus.ACCEPTED);
        request.setRespondedAt(LocalDateTime.now());

        FollowRequest updatedRequest = followRequestRepository.save(request);

        // Create notification for the follower
        notificationService.createFollowAcceptedNotification(
                request.getFollower(), request.getFollowing());

        return convertToDTO(updatedRequest);
    }

    public FollowRequestDTO rejectFollowRequest(Long requestId) {
        FollowRequest request = followRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request already processed");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRespondedAt(LocalDateTime.now());

        return convertToDTO(followRequestRepository.save(request));
    }

    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FollowRequest request = followRequestRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new RuntimeException("Not following"));

        followRequestRepository.delete(request);
    }

    public List<FollowRequestDTO> getPendingRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return followRequestRepository.findByFollowingAndStatus(user, RequestStatus.PENDING)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getPendingRequestCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return followRequestRepository.countByFollowingAndStatus(user, RequestStatus.PENDING);
    }

    public String getFollowStatus(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        var request = followRequestRepository.findByFollowerAndFollowing(user, target);

        if (request.isEmpty()) {
            return "NOT_FOLLOWING";
        }

        return request.get().getStatus().toString();
    }

    private FollowRequestDTO convertToDTO(FollowRequest request) {
        FollowRequestDTO dto = new FollowRequestDTO();
        dto.setId(request.getId());
        dto.setStatus(request.getStatus().toString());
        dto.setCreatedAt(request.getCreatedAt());

        UserDTO followerDTO = new UserDTO();
        followerDTO.setId(request.getFollower().getId());
        followerDTO.setFirstName(request.getFollower().getFirstName());
        followerDTO.setLastName(request.getFollower().getLastName());
        followerDTO.setEmail(request.getFollower().getEmail());
        followerDTO.setProfilePicture(request.getFollower().getProfilePicture());
        dto.setFollower(followerDTO);

        UserDTO followingDTO = new UserDTO();
        followingDTO.setId(request.getFollowing().getId());
        followingDTO.setFirstName(request.getFollowing().getFirstName());
        followingDTO.setLastName(request.getFollowing().getLastName());
        followingDTO.setEmail(request.getFollowing().getEmail());
        followingDTO.setProfilePicture(request.getFollowing().getProfilePicture());
        dto.setFollowing(followingDTO);

        return dto;
    }
}