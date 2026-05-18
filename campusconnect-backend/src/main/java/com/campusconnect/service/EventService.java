package com.campusconnect.service;

import com.campusconnect.dto.EventDTO;
import com.campusconnect.dto.UserDTO;
import com.campusconnect.model.Event;
import com.campusconnect.model.EventAttendee;
import com.campusconnect.model.User;
import com.campusconnect.repository.EventAttendeeRepository;
import com.campusconnect.repository.EventRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventAttendeeRepository eventAttendeeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<EventDTO> getAllEvents(Long userId) {
        return eventRepository.findAllByOrderByEventDateDesc().stream()
                .map(event -> convertToDTO(event, userId))
                .collect(Collectors.toList());
    }

    public List<EventDTO> getUpcomingEvents(Long userId) {
        return eventRepository.findByEventDateAfterOrderByEventDateAsc(LocalDateTime.now()).stream()
                .map(event -> convertToDTO(event, userId))
                .collect(Collectors.toList());
    }

    public EventDTO getEventById(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return convertToDTO(event, userId);
    }

    public EventDTO createEvent(Long userId, Event event) {
        User organizer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        event.setOrganizer(organizer);
        Event savedEvent = eventRepository.save(event);

        return convertToDTO(savedEvent, userId);
    }

    public EventDTO updateEvent(Long eventId, Long userId, Event updatedEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Check if user is the organizer
        if (!event.getOrganizer().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to edit this event");
        }

        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setLocation(updatedEvent.getLocation());
        event.setEventDate(updatedEvent.getEventDate());
        event.setEventEndDate(updatedEvent.getEventEndDate());
        event.setCategory(updatedEvent.getCategory());
        if (updatedEvent.getImageUrl() != null) {
            event.setImageUrl(updatedEvent.getImageUrl());
        }

        Event savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent, userId);
    }

    public void deleteEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Check if user is the organizer
        if (!event.getOrganizer().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this event");
        }

        // Delete all attendees first
        eventAttendeeRepository.deleteAll(eventAttendeeRepository.findByEvent(event));

        // Delete the event
        eventRepository.delete(event);
    }

    public EventDTO registerForEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if already registered
        if (eventAttendeeRepository.existsByUserAndEvent(user, event)) {
            throw new RuntimeException("Already registered for this event");
        }

        // Create attendance
        EventAttendee attendee = new EventAttendee();
        attendee.setUser(user);
        attendee.setEvent(event);
        eventAttendeeRepository.save(attendee);

        // Update event attendees count
        event.setAttendeesCount(event.getAttendeesCount() + 1);
        eventRepository.save(event);

        return convertToDTO(event, userId);
    }

    public EventDTO unregisterFromEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find and delete attendance
        EventAttendee attendee = eventAttendeeRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new RuntimeException("Not registered for this event"));

        eventAttendeeRepository.delete(attendee);

        // Update event attendees count
        event.setAttendeesCount(Math.max(0, event.getAttendeesCount() - 1));
        eventRepository.save(event);

        return convertToDTO(event, userId);
    }

    private EventDTO convertToDTO(Event event, Long userId) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setEventDate(event.getEventDate());
        dto.setEventEndDate(event.getEventEndDate());
        dto.setCategory(event.getCategory());
        dto.setImageUrl(event.getImageUrl());
        dto.setAttendeesCount(event.getAttendeesCount());
        dto.setCreatedAt(event.getCreatedAt());

        // Convert organizer
        UserDTO organizerDTO = new UserDTO();
        organizerDTO.setId(event.getOrganizer().getId());
        organizerDTO.setFirstName(event.getOrganizer().getFirstName());
        organizerDTO.setLastName(event.getOrganizer().getLastName());
        organizerDTO.setEmail(event.getOrganizer().getEmail());
        dto.setOrganizer(organizerDTO);

        // Check if user is attending
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                dto.setAttending(eventAttendeeRepository.existsByUserAndEvent(user, event));
            }
        }

        return dto;
    }
}