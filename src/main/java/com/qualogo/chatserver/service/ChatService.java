package com.qualogo.chatserver.service;

import com.qualogo.chatserver.models.ChatMessage;
import com.qualogo.chatserver.repository.ChatMessageRepository;
import com.qualogo.chatserver.security.services.UserDetailsImpl;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private RateLimitingService rateLimitingService;

    private static final String CHAT_ROOM_MESSAGES = "chatRoomMessages";
    private static final String CHAT_ROOM_MESSAGES_BY_TIME = "chatRoomMessagesByTime";
    private static final String RATE_LIMIT_EXCEEDED = "Rate limit exceeded. Please try again later.";

    /**
     * Retrieves the current authenticated user's username.
     *
     * @return the username of the current authenticated user.
     */
    public static String getCurrentUser() {
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

    /**
     * Allows a user to join a chat room.
     *
     * @return a welcome message if the user is allowed to join, otherwise a rate limit exceeded message.
     */
    public String joinRoom() {
        String username = getCurrentUser();
        if (!rateLimitingService.isAllowed(username)) {
            return RATE_LIMIT_EXCEEDED;
        }
        return String.format("Welcome %s, joined the chat room.", username);
    }

    /**
     * Sends a message to the chat room.
     *
     * @param message the message to be sent.
     * @return a success message if the message is sent, otherwise an error message.
     */
    public String sendMessage(String message) {
        try {
            String username = getCurrentUser();
            if (!rateLimitingService.isAllowed(username)) {
                return RATE_LIMIT_EXCEEDED;
            }
            
            ChatMessage chatMessage = new ChatMessage(username, message);
            chatMessageRepository.save(chatMessage);

            // Store message in Redis
            long messageId = chatMessage.getId();
            long timestamp = chatMessage.getTimestamp();

            redisTemplate.opsForHash().put("message:" + messageId, "id", messageId);
            redisTemplate.opsForHash().put("message:" + messageId, "content", message);
            redisTemplate.opsForHash().put("message:" + messageId, "user", username);
            redisTemplate.opsForHash().put("message:" + messageId, "timestamp", timestamp);

            redisTemplate.opsForList().rightPush(CHAT_ROOM_MESSAGES, messageId);
            redisTemplate.opsForZSet().add(CHAT_ROOM_MESSAGES_BY_TIME, messageId, timestamp);

            return "Message sent!";
        } catch (Exception ex) {
            return "Error";
        }
    }

    /**
     * Retrieves messages from the chat room since a given timestamp.
     *
     * @param timestamp the timestamp from which to retrieve messages.
     * @return a list of chat messages since the given timestamp.
     */
    @Cacheable(value = "chatMessages", key = "#timestamp")
    public List<ChatMessage> getMessagesSince(long timestamp) {
        String username = getCurrentUser();
        if (!rateLimitingService.isAllowed(username)) {
            return Collections.emptyList(); 
        }
        
        Set<Object> messageIds = redisTemplate.opsForZSet().rangeByScore(CHAT_ROOM_MESSAGES_BY_TIME, timestamp, System.currentTimeMillis());
        List<ChatMessage> messages = new ArrayList<>();

        for (Object id : messageIds) {
            Map<Object, Object> messageMap = redisTemplate.opsForHash().entries("message:" + id);
            ChatMessage message = new ChatMessage();
            message.setId((Long) messageMap.get("id"));
            message.setUsername((String) messageMap.get("user"));
            message.setMessage((String) messageMap.get("content"));
            message.setTimestamp((Long) messageMap.get("timestamp"));
            messages.add(message);
        }

        //worst case
        if (messages.isEmpty()) {
            // If cache is empty, retrieve messages from the database and cache them
            messages = chatMessageRepository.findAllByTimestampGreaterThan(timestamp);
            redisTemplate.opsForList().rightPushAll("chatMessages", messages);
            redisTemplate.expire("chatMessages", 1, TimeUnit.DAYS);
        }
        return messages;
    }

    /**
     * Deletes a message from the chat room.
     *
     * @param messageId the ID of the message to be deleted.
     * @return a success message if the message is deleted, otherwise an error message.
     */
    public String deleteMessage(long messageId) {
        String username = getCurrentUser();
        if (!rateLimitingService.isAllowed(username)) {
            return RATE_LIMIT_EXCEEDED;
        }
    
        // Fetch the message from the repository
        Optional<ChatMessage> optionalMessage = chatMessageRepository.findById(messageId);
        
        if (optionalMessage.isPresent()) {
            ChatMessage chatMessage = optionalMessage.get();
            
            // Check if the message belongs to the current user
            if (chatMessage.getUsername().equals(username)) {
                // Remove message from Redis
                redisTemplate.opsForHash().delete("message:" + messageId);
                redisTemplate.opsForList().remove(CHAT_ROOM_MESSAGES, 1, messageId);
                redisTemplate.opsForZSet().remove(CHAT_ROOM_MESSAGES_BY_TIME, messageId);
    
                // Delete the message from the repository
                chatMessageRepository.deleteById(messageId);
                return "Successfully deleted the message";
            } else {
                return "You can only delete your own messages";
            }
        } else {
            return "Message not found";
        }
    }
}