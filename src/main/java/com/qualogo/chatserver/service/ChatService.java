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

    public String joinRoom() {
        String username = getCurrentUser();
        if (!rateLimitingService.isAllowed(username)) {
            return RATE_LIMIT_EXCEEDED;
        }
        return String.format("Welcome %s, joined the chat room.", username);
    }

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

    public String deleteMessage(long messageId) {
        String username = getCurrentUser();
        if (!rateLimitingService.isAllowed(username)) {
            return RATE_LIMIT_EXCEEDED;
        }
        
        if (chatMessageRepository.existsById(messageId)) {
            // Remove message from Redis
            redisTemplate.opsForHash().delete("message:" + messageId);
            redisTemplate.opsForList().remove(CHAT_ROOM_MESSAGES, 1, messageId);
            redisTemplate.opsForZSet().remove(CHAT_ROOM_MESSAGES_BY_TIME, messageId);

            chatMessageRepository.deleteById(messageId);
            return "Successfully deleted the message";
        }
        return "Message not found";

    }
}
