package com.campusconnect.repository;

import com.campusconnect.model.Notification;
import com.campusconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    List<Notification> findByUserAndIsReadFalse(User user);  // ADD THIS LINE
    Long countByUserAndIsReadFalse(User user);
}