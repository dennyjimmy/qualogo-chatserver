package com.qualogo.chatserver.service;

import com.qualogo.chatserver.model.ChatMessage;
import com.qualogo.chatserver.repository.ChatMessageRepository;
import com.qualogo.chatserver.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private RateLimitingService rateLimitingService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(1L, "testUser", "test@example.com", "password", Collections.emptyList()));
        when(authentication.getName()).thenReturn("testUser");
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testJoinRoomAllowed() {
        when(rateLimitingService.isAllowed(anyString())).thenReturn(true);

        String result = chatService.joinRoom();

        assertEquals("Welcome testUser, joined the chat room.", result);
    }

    @Test
    void testJoinRoomRateLimitExceeded() {
        when(rateLimitingService.isAllowed(anyString())).thenReturn(false);

        String result = chatService.joinRoom();

        assertEquals("Rate limit exceeded. Please try again later.", result);
    }

    @Test
    void testSendMessageAllowed() {
        when(rateLimitingService.isAllowed(anyString())).thenReturn(true);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> {
            ChatMessage message = invocation.getArgument(0);
            message.setId(1L);
            message.setTimestamp(System.currentTimeMillis());
            return message;
        });

        String result = chatService.sendMessage("Hello World");

        assertEquals("Message sent!", result);
    }

    @Test
    void testSendMessageRateLimitExceeded() {
        when(rateLimitingService.isAllowed(anyString())).thenReturn(false);

        String result = chatService.sendMessage("Hello World");

        assertEquals("Rate limit exceeded. Please try again later.", result);
    }

    @Test
    void testGetMessagesSinceAllowed() {
        when(rateLimitingService.isAllowed(anyString())).thenReturn(true);
        Set<Object> messageIds = new HashSet<>();
        messageIds.add(1L);
        when(redisTemplate.opsForZSet().rangeByScore(anyString(), anyDouble(), anyDouble())).thenReturn(messageIds);
        Map<Object, Object> messageMap = new HashMap<>();
        messageMap.put("id", 1L);
        messageMap.put("user", "testUser");
        messageMap.put("content", "Hello World");
        messageMap.put("timestamp", System.currentTimeMillis());
        when(redisTemplate.opsForHash().entries(anyString())).thenReturn(messageMap);

        List<ChatMessage> messages = chatService.getMessagesSince(System.currentTimeMillis() - 1000);

        assertFalse(messages.isEmpty());
        assertEquals(1, messages.size());
        assertEquals("Hello World", messages.get(0).getMessage());
    }

    @Test
    void testGetMessagesSinceRateLimitExceeded() {
        when(rateLimitingService.isAllowed(anyString())).thenReturn(false);

        List<ChatMessage> messages = chatService.getMessagesSince(System.currentTimeMillis() - 1000);

        assertTrue(messages.isEmpty());
    }

    @Test
    void testDeleteMessageAllowed() {
        when(rateLimitingService.isAllowed(anyString())).thenReturn(true);
        when(chatMessageRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(chatMessageRepository).deleteById(anyLong());

        String result = chatService.deleteMessage(1L);

        assertEquals("Successfully deleted the message", result);
    }

    @Test
    void testDeleteMessageRateLimitExceeded() {
        when(rateLimitingService.isAllowed(anyString())).thenReturn(false);

        String result = chatService.deleteMessage(1L);

        assertEquals("Rate limit exceeded. Please try again later.", result);
    }

    @Test
    void testDeleteMessageNotFound() {
        when(rateLimitingService.isAllowed(anyString())).thenReturn(true);
        when(chatMessageRepository.existsById(anyLong())).thenReturn(false);

        this.chatMessageRepository = chatMessageRepository;
        this.rateLimitingService = rateLimitingService;
        
        String result = chatService.deleteMessage(1L);

        assertEquals("Message not found", result);
    }
}
