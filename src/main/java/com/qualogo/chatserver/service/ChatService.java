package com.qualogo.chatserver.service;

import com.qualogo.chatserver.model.ChatMessage;
import com.qualogo.chatserver.repository.ChatMessageRepository;
import com.qualogo.chatserver.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private static String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (authentication.getPrincipal() instanceof UserDetailsImpl customUserDetails) {
            // You can now access the custom user details
            return customUserDetails.getUsername();
        } else {
            // Handle cases where the principal is not an instance of CustomUserDetails
            return "Username: " + username;
        }
    }

    public String joinRoom() {
        String username = getCurrentUser();
        return String.format("Welcome %s, joined the chat room.", username);
    }

    public String sendMessage(String message) {
        String username = getCurrentUser();
        ChatMessage chatMessage = new ChatMessage(username, message);
        chatMessageRepository.save(chatMessage);
        return "Message sent!";
    }

    @Cacheable(value = "chatMessages")
    public List<ChatMessage> getMessagesSince(long timestamp) {
        return chatMessageRepository.findAllByTimestampGreaterThan(timestamp);
    }

    public String deleteMessage(long messageId) {
        chatMessageRepository.deleteById(messageId);
        return "Message deleted!";
    }
}
