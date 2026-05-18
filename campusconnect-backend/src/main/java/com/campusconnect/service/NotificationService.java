package com.campusconnect.service;

import com.campusconnect.model.*;
import com.campusconnect.repository.NotificationRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public void createLikeNotification(User postAuthor, User liker, Post post) {
        Notification notification = new Notification();
        notification.setUser(postAuthor);
        notification.setActor(liker);
        notification.setType(NotificationType.LIKE);
        notification.setPost(post);
        notification.setMessage(liker.getFirstName() + " " + liker.getLastName() + " liked your post");

        notificationRepository.save(notification);
    }

    public void createCommentNotification(User postAuthor, User commenter, Post post) {
        Notification notification = new Notification();
        notification.setUser(postAuthor);
        notification.setActor(commenter);
        notification.setType(NotificationType.COMMENT);
        notification.setPost(post);
        notification.setMessage(commenter.getFirstName() + " " + commenter.getLastName() + " commented on your post");

        notificationRepository.save(notification);
    }

    public void createFollowRequestNotification(User receiver, User sender) {
        Notification notification = new Notification();
        notification.setUser(receiver);
        notification.setActor(sender);
        notification.setType(NotificationType.FOLLOW_REQUEST);
        notification.setMessage(sender.getFirstName() + " " + sender.getLastName() + " sent you a follow request");

        notificationRepository.save(notification);
    }

    public void createFollowAcceptedNotification(User requester, User accepter) {
        Notification notification = new Notification();
        notification.setUser(requester);
        notification.setActor(accepter);
        notification.setType(NotificationType.FOLLOW_ACCEPTED);
        notification.setMessage(accepter.getFirstName() + " " + accepter.getLastName() + " accepted your follow request");

        notificationRepository.save(notification);
    }

    public void createJobApplicationNotification(User jobPoster, User applicant, JobPosting job) {
        Notification notification = new Notification();
        notification.setUser(jobPoster);
        notification.setActor(applicant);
        notification.setType(NotificationType.JOB_APPLICATION);
        notification.setMessage(applicant.getFirstName() + " " + applicant.getLastName() +
                " applied for " + job.getTitle());

        notificationRepository.save(notification);
    }

    public void createApplicationStatusNotification(User applicant, JobPosting job, ApplicationStatus status) {
        Notification notification = new Notification();
        notification.setUser(applicant);
        notification.setActor(job.getPostedBy());
        notification.setType(NotificationType.APPLICATION_STATUS);
        notification.setMessage("Your application for " + job.getTitle() + " is now " + status.toString());

        notificationRepository.save(notification);
    }

    // VERIFICATION NOTIFICATIONS
    public void createAccountVerificationNotification(User user, boolean approved, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setActor(null);  // System notification - no actor
        notification.setType(NotificationType.ACCOUNT_VERIFICATION);

        if (approved) {
            notification.setMessage("✅ Your account has been approved! You can now access all features.");
        } else {
            notification.setMessage("❌ Your account verification was not approved. Reason: " + message);
        }

        notificationRepository.save(notification);
    }

    public void notifyAdminsNewRegistration(User newUser) {
        List<User> admins = userRepository.findByRole(UserRole.ADMIN);

        for (User admin : admins) {
            Notification notification = new Notification();
            notification.setUser(admin);
            notification.setActor(newUser);
            notification.setType(NotificationType.NEW_REGISTRATION);
            notification.setMessage(newUser.getFirstName() + " " + newUser.getLastName() +
                    " (" + newUser.getRole() + ") has registered and needs verification.");
            notificationRepository.save(notification);
        }
    }

    public List<Notification> getUserNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void markAllAsRead(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Notification> notifications = notificationRepository.findByUserAndIsReadFalse(user);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    public Long getUnreadCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    public void createMessageNotification(User receiver, User sender) {
        Notification notification = new Notification();
        notification.setUser(receiver);
        notification.setActor(sender);
        notification.setType(NotificationType.MESSAGE);
        notification.setMessage(sender.getFirstName() + " " + sender.getLastName() + " sent you a message");

        notificationRepository.save(notification);
    }
}