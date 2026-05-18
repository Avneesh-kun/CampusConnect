package com.campusconnect.controller;

import com.campusconnect.dto.ConversationDTO;
import com.campusconnect.dto.MessageDTO;
import com.campusconnect.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
//@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> request) {
        try {
            Long senderId = Long.valueOf(request.get("senderId").toString());
            Long receiverId = Long.valueOf(request.get("receiverId").toString());
            String content = request.get("content").toString();

            return ResponseEntity.ok(messageService.sendMessage(senderId, receiverId, content));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<MessageDTO>> getConversation(
            @RequestParam Long user1Id,
            @RequestParam Long user2Id) {
        return ResponseEntity.ok(messageService.getConversation(user1Id, user2Id));
    }

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<ConversationDTO>> getUserConversations(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getUserConversations(userId));
    }

    @PostMapping("/mark-read")
    public ResponseEntity<Void> markAsRead(@RequestBody Map<String, Long> request) {
        Long user1Id = request.get("user1Id");
        Long user2Id = request.get("user2Id");
        messageService.markAsRead(user1Id, user2Id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<Long> getUnreadMessageCount(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getUnreadMessageCount(userId));
    }
}