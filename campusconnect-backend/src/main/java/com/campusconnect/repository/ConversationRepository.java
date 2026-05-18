package com.campusconnect.repository;

import com.campusconnect.model.Conversation;
import com.campusconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c WHERE " +
            "(c.user1 = ?1 AND c.user2 = ?2) OR (c.user1 = ?2 AND c.user2 = ?1)")
    Optional<Conversation> findConversationBetween(User user1, User user2);

    @Query("SELECT c FROM Conversation c WHERE c.user1 = ?1 OR c.user2 = ?1 " +
            "ORDER BY c.lastMessageTime DESC")
    List<Conversation> findUserConversations(User user);
}