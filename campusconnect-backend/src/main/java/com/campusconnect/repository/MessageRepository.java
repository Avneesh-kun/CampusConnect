package com.campusconnect.repository;

import com.campusconnect.model.Message;
import com.campusconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = ?1 AND m.receiver = ?2) OR (m.sender = ?2 AND m.receiver = ?1) " +
            "ORDER BY m.createdAt ASC")
    List<Message> findConversationBetween(User user1, User user2);

    long countByReceiverAndIsReadFalse(User receiver);

    List<Message> findByReceiverAndIsReadFalse(User receiver);
}