package com.campusconnect.repository;

import com.campusconnect.model.Event;
import com.campusconnect.model.EventAttendee;
import com.campusconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {
    Optional<EventAttendee> findByUserAndEvent(User user, Event event);
    List<EventAttendee> findByEvent(Event event);
    List<EventAttendee> findByUser(User user);
    boolean existsByUserAndEvent(User user, Event event);
    long countByEvent(Event event);
}