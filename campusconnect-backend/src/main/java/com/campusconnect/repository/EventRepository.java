package com.campusconnect.repository;

import com.campusconnect.model.Event;
import com.campusconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByOrderByEventDateDesc();
    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDateTime date);
    List<Event> findByOrganizerOrderByEventDateDesc(User organizer);
}