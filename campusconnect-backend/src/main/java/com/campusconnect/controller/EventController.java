package com.campusconnect.controller;

import com.campusconnect.dto.EventDTO;
import com.campusconnect.model.Event;
import com.campusconnect.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
//@CrossOrigin(origins = "http://localhost:3000")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean upcoming) {
        if (upcoming) {
            return ResponseEntity.ok(eventService.getUpcomingEvents(userId));
        }
        return ResponseEntity.ok(eventService.getAllEvents(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(eventService.getEventById(id, userId));
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());

            Event event = new Event();
            event.setTitle(request.get("title").toString());
            event.setDescription(request.get("description").toString());
            event.setLocation(request.get("location").toString());
            event.setEventDate(java.time.LocalDateTime.parse(request.get("eventDate").toString()));

            if (request.containsKey("eventEndDate") && request.get("eventEndDate") != null) {
                event.setEventEndDate(java.time.LocalDateTime.parse(request.get("eventEndDate").toString()));
            }

            event.setCategory(request.get("category").toString());

            if (request.containsKey("imageUrl")) {
                event.setImageUrl(request.get("imageUrl").toString());
            }

            return ResponseEntity.ok(eventService.createEvent(userId, event));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());

            Event event = new Event();
            event.setTitle(request.get("title").toString());
            event.setDescription(request.get("description").toString());
            event.setLocation(request.get("location").toString());
            event.setEventDate(java.time.LocalDateTime.parse(request.get("eventDate").toString()));

            if (request.containsKey("eventEndDate") && request.get("eventEndDate") != null) {
                event.setEventEndDate(java.time.LocalDateTime.parse(request.get("eventEndDate").toString()));
            }

            event.setCategory(request.get("category").toString());

            if (request.containsKey("imageUrl")) {
                event.setImageUrl(request.get("imageUrl").toString());
            }

            return ResponseEntity.ok(eventService.updateEvent(id, userId, event));
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id,
            @RequestParam Long userId) {
        try {
            eventService.deleteEvent(id, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<EventDTO> registerForEvent(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        return ResponseEntity.ok(eventService.registerForEvent(id, userId));
    }

    @PostMapping("/{id}/unregister")
    public ResponseEntity<EventDTO> unregisterFromEvent(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        return ResponseEntity.ok(eventService.unregisterFromEvent(id, userId));
    }
}