package com.campusconnect.service;

import com.campusconnect.dto.ConversationDTO;
import com.campusconnect.dto.MessageDTO;
import com.campusconnect.dto.UserDTO;
import com.campusconnect.model.Conversation;
import com.campusconnect.model.Message;
import com.campusconnect.model.User;
import com.campusconnect.repository.ConversationRepository;
import com.campusconnect.repository.MessageRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public MessageDTO sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Create message
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);

        Message savedMessage = messageRepository.save(message);

        // Update or create conversation
        Conversation conversation = conversationRepository
                .findConversationBetween(sender, receiver)
                .orElse(new Conversation());

        if (conversation.getId() == null) {
            conversation.setUser1(sender);
            conversation.setUser2(receiver);
        }

        conversation.setLastMessage(content);
        conversation.setLastMessageTime(LocalDateTime.now());
        conversation.setUnreadCount(conversation.getUnreadCount() + 1);
        conversationRepository.save(conversation);

        // Create notification
        notificationService.createMessageNotification(receiver, sender);

        return convertMessageToDTO(savedMessage);
    }

    public List<MessageDTO> getConversation(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return messageRepository.findConversationBetween(user1, user2).stream()
                .map(this::convertMessageToDTO)
                .collect(Collectors.toList());
    }

    public List<ConversationDTO> getUserConversations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return conversationRepository.findUserConversations(user).stream()
                .map(conversation -> convertConversationToDTO(conversation, user))
                .collect(Collectors.toList());
    }

    public void markAsRead(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Message> messages = messageRepository.findConversationBetween(user1, user2);
        messages.stream()
                .filter(m -> m.getReceiver().getId().equals(user1Id) && !m.isRead())
                .forEach(m -> m.setRead(true));
        messageRepository.saveAll(messages);

        // Update conversation unread count
        conversationRepository.findConversationBetween(user1, user2)
                .ifPresent(conversation -> {
                    conversation.setUnreadCount(0);
                    conversationRepository.save(conversation);
                });
    }

    public long getUnreadMessageCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return messageRepository.countByReceiverAndIsReadFalse(user);
    }

    private MessageDTO convertMessageToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setRead(message.isRead());
        dto.setCreatedAt(message.getCreatedAt());

        UserDTO senderDTO = new UserDTO();
        senderDTO.setId(message.getSender().getId());
        senderDTO.setFirstName(message.getSender().getFirstName());
        senderDTO.setLastName(message.getSender().getLastName());
        senderDTO.setProfilePicture(message.getSender().getProfilePicture());
        dto.setSender(senderDTO);

        UserDTO receiverDTO = new UserDTO();
        receiverDTO.setId(message.getReceiver().getId());
        receiverDTO.setFirstName(message.getReceiver().getFirstName());
        receiverDTO.setLastName(message.getReceiver().getLastName());
        receiverDTO.setProfilePicture(message.getReceiver().getProfilePicture());
        dto.setReceiver(receiverDTO);

        return dto;
    }

    private ConversationDTO convertConversationToDTO(Conversation conversation, User currentUser) {
        ConversationDTO dto = new ConversationDTO();
        dto.setId(conversation.getId());
        dto.setLastMessage(conversation.getLastMessage());
        dto.setLastMessageTime(conversation.getLastMessageTime());
        dto.setUnreadCount(conversation.getUnreadCount());

        // Determine the other user
        User otherUser = conversation.getUser1().getId().equals(currentUser.getId())
                ? conversation.getUser2()
                : conversation.getUser1();

        UserDTO otherUserDTO = new UserDTO();
        otherUserDTO.setId(otherUser.getId());
        otherUserDTO.setFirstName(otherUser.getFirstName());
        otherUserDTO.setLastName(otherUser.getLastName());
        otherUserDTO.setEmail(otherUser.getEmail());
        otherUserDTO.setProfilePicture(otherUser.getProfilePicture());
        dto.setOtherUser(otherUserDTO);

        return dto;
    }
}