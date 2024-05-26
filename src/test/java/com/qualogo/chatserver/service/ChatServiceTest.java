package com.qualogo.chatserver.service;

import com.qualogo.chatserver.models.ChatMessage;
import static org.mockito.Mockito.*;

import com.qualogo.chatserver.repository.ChatMessageRepository;
import com.qualogo.chatserver.security.services.UserDetailsImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private RateLimitingService rateLimitingService;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testGetCurrentUserWithUserDetailsImpl() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");

        String result = ChatService.getCurrentUser();
        assertEquals("testUser", result);
    }

    @Test
    public void testGetCurrentUserWithUsername() {
        when(authentication.getPrincipal()).thenReturn("testUser");

        String result = ChatService.getCurrentUser();
        assertEquals("Username: null", result);
    }

    @Test
    public void testGetCurrentUserWithNullPrincipal() {
        when(authentication.getPrincipal()).thenReturn(null);
        when(authentication.getName()).thenReturn("testUser");

        String result = ChatService.getCurrentUser();
        assertEquals("Username: testUser", result);
    }

    @Test
    public void testGetCurrentUserWithEmptyUsername() {
        when(authentication.getPrincipal()).thenReturn(null);
        when(authentication.getName()).thenReturn("");

        String result = ChatService.getCurrentUser();
        assertEquals("Username: ", result);
    }

    @Test
    public void testGetCurrentUserWithNullAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        String result = ChatService.getCurrentUser();
        assertEquals("Username: null", result);
    }

    @Test
    public void testGetCurrentUserWithException() {
        when(securityContext.getAuthentication()).thenThrow(new RuntimeException("Test Exception"));

        String result = ChatService.getCurrentUser();
        assertEquals("Username: null", result);
    }

    @Test
    public void testGetCurrentUserWithAnonymousUser() {
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        String result = ChatService.getCurrentUser();
        assertEquals("Username: anonymousUser", result);
    }

    @Test
    public void testGetCurrentUserWithCustomUserDetails() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("customUser");

        String result = ChatService.getCurrentUser();
        assertEquals("customUser", result);
    }

    @Test
    public void testGetCurrentUserWithNullUsername() {
        when(authentication.getPrincipal()).thenReturn(null);
        when(authentication.getName()).thenReturn(null);

        String result = ChatService.getCurrentUser();
        assertEquals("Username: null", result);
    }

    @Test
    public void testGetCurrentUserWithEmptyPrincipal() {
        when(authentication.getPrincipal()).thenReturn("");

        String result = ChatService.getCurrentUser();
        assertEquals("Username: ", result);
    }

    @Test
    public void testGetCurrentUserWithNullSecurityContext() {
        SecurityContextHolder.setContext(null);

        String result = ChatService.getCurrentUser();
        assertEquals("Username: null", result);
    }

    @Test
    public void testGetCurrentUserWithNullSecurityContextAndAuthentication() {
        SecurityContextHolder.setContext(null);
        when(securityContext.getAuthentication()).thenReturn(null);

        String result = ChatService.getCurrentUser();
        assertEquals("Username: null", result);
    }

    @Test
    public void testGetCurrentUserWithNullSecurityContextAndPrincipal() {
        SecurityContextHolder.setContext(null);
        when(authentication.getPrincipal()).thenReturn(null);

        String result = ChatService.getCurrentUser();
        assertEquals("Username: null", result);
    }

    @Test
    public void testGetCurrentUserWithNullSecurityContextAndUsername() {
        SecurityContextHolder.setContext(null);
        when(authentication.getName()).thenReturn(null);

        String result = ChatService.getCurrentUser();
        assertEquals("Username: null", result);
    }

    @Test
    public void testGetCurrentUserWithNullSecurityContextAndEmptyUsername() {
        SecurityContextHolder.setContext(null);
        when(authentication.getName()).thenReturn("");

        String result = ChatService.getCurrentUser();
        assertEquals("Username: ", result);
    }

    @Test
    public void testGetCurrentUserWithNullSecurityContextAndEmptyPrincipal() {
        SecurityContextHolder.setContext(null);
        when(authentication.getPrincipal()).thenReturn("");

        String result = ChatService.getCurrentUser();
        assertEquals("Username: ", result);
    }

    @Test
    public void testGetCurrentUserWithNullSecurityContextAndAnonymousUser() {
        SecurityContextHolder.setContext(null);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        String result = ChatService.getCurrentUser();
        assertEquals("Username: anonymousUser", result);
    }

    @Test
    public void testGetCurrentUserWithNullSecurityContextAndCustomUserDetails() {
        SecurityContextHolder.setContext(null);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("customUser");

        String result = ChatService.getCurrentUser();
        assertEquals("customUser", result);
    }

    @Test
    public void testGetCurrentUserWithNullSecurityContextAndNullUsername() {
        SecurityContextHolder.setContext(null);
        when(authentication.getPrincipal()).thenReturn(null);
        when(authentication.getName()).thenReturn(null);

        String result = ChatService.getCurrentUser();
        assertEquals("Username: null", result);
    }

    @Test
    public void testGetCurrentUserWithNullSecurityContextAndNullAuthentication() {
        SecurityContextHolder.setContext(null);
        when(securityContext.getAuthentication()).thenReturn(null);

        String result = ChatService.getCurrentUser();
        assertEquals("Username: null", result);
    }

    @Test
    public void testJoinRoomWithValidUser() {
        when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl());
        when(rateLimitingService.isAllowed(anyString())).thenReturn(true);

        String result = chatService.joinRoom();
        assertEquals("Welcome testUser, joined the chat room.", result);
    }

    @Test
    public void testJoinRoomWithRateLimitExceeded() {
        when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl());
        when(rateLimitingService.isAllowed(anyString())).thenReturn(false);

        String result = chatService.joinRoom();
        assertEquals("Rate limit exceeded. Please try again later.", result);
    }

    @Test
    public void testJoinRoomWithNullPrincipal() {
        when(authentication.getPrincipal()).thenReturn(null);
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed(anyString())).thenReturn(true);

        String result = chatService.joinRoom();
        assertEquals("Welcome Username: testUser, joined the chat room.", result);
    }

    @Test
    public void testJoinRoomWithEmptyUsername() {
        when(authentication.getPrincipal()).thenReturn(null);
        when(authentication.getName()).thenReturn("");
        when(rateLimitingService.isAllowed(anyString())).thenReturn(true);

        String result = chatService.joinRoom();
        assertEquals("Welcome Username: , joined the chat room.", result);
    }

    @Test
    public void testJoinRoomWithException() {
        when(authentication.getPrincipal()).thenThrow(new RuntimeException("Test Exception"));

        String result = chatService.joinRoom();
        assertEquals("Rate limit exceeded. Please try again later.", result);
    }

    @Test
    public void testSendMessageSuccess() {
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(true);

        ChatMessage chatMessage = new ChatMessage("testUser", "Hello");
        chatMessage.setId(1L);
        chatMessage.setTimestamp(System.currentTimeMillis());

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        String result = chatService.sendMessage("Hello");
        assertEquals("Message sent!", result);

        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(redisTemplate).opsForHash().put("message:1", "id", 1L);
        verify(redisTemplate).opsForHash().put("message:1", "content", "Hello");
        verify(redisTemplate).opsForHash().put("message:1", "user", "testUser");
        verify(redisTemplate).opsForHash().put("message:1", "timestamp", chatMessage.getTimestamp());
        verify(redisTemplate).opsForList().rightPush("chatRoomMessages", 1L);
        verify(redisTemplate).opsForZSet().add("chatRoomMessagesByTime", 1L, chatMessage.getTimestamp());
    }

    @Test
    public void testSendMessageRateLimitExceeded() {
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(false);

        String result = chatService.sendMessage("Hello");
        assertEquals("Rate limit exceeded. Please try again later.", result);

        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
        verify(redisTemplate, never()).opsForHash();
        verify(redisTemplate, never()).opsForList();
        verify(redisTemplate, never()).opsForZSet();
    }

    @Test
    public void testSendMessageWithException() {
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(true);

        when(chatMessageRepository.save(any(ChatMessage.class))).thenThrow(new RuntimeException("Database error"));

        String result = chatService.sendMessage("Hello");
        assertEquals("Error", result);

        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(redisTemplate, never()).opsForHash();
        verify(redisTemplate, never()).opsForList();
        verify(redisTemplate, never()).opsForZSet();
    }

    @Test
    public void testSendMessageWithNullMessage() {
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(true);

        String result = chatService.sendMessage(null);
        assertEquals("Error", result);

        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
        verify(redisTemplate, never()).opsForHash();
        verify(redisTemplate, never()).opsForList();
        verify(redisTemplate, never()).opsForZSet();
    }

    @Test
    public void testSendMessageWithEmptyMessage() {
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(true);

        String result = chatService.sendMessage("");
        assertEquals("Error", result);

        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
        verify(redisTemplate, never()).opsForHash();
        verify(redisTemplate, never()).opsForList();
        verify(redisTemplate, never()).opsForZSet();
    }

    @Test
    public void testSendMessageWithLongMessage() {
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(true);

        String longMessage = "a".repeat(1000);
        ChatMessage chatMessage = new ChatMessage("testUser", longMessage);
        chatMessage.setId(1L);
        chatMessage.setTimestamp(System.currentTimeMillis());

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        String result = chatService.sendMessage(longMessage);
        assertEquals("Message sent!", result);

        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(redisTemplate).opsForHash().put("message:1", "id", 1L);
        verify(redisTemplate).opsForHash().put("message:1", "content", longMessage);
        verify(redisTemplate).opsForHash().put("message:1", "user", "testUser");
        verify(redisTemplate).opsForHash().put("message:1", "timestamp", chatMessage.getTimestamp());
        verify(redisTemplate).opsForList().rightPush("chatRoomMessages", 1L);
        verify(redisTemplate).opsForZSet().add("chatRoomMessagesByTime", 1L, chatMessage.getTimestamp());
    }

    @Test
    public void testSendMessageWithSpecialCharacters() {
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(true);

        String specialMessage = "!@#$%^&*()_+";
        ChatMessage chatMessage = new ChatMessage("testUser", specialMessage);
        chatMessage.setId(1L);
        chatMessage.setTimestamp(System.currentTimeMillis());

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        String result = chatService.sendMessage(specialMessage);
        assertEquals("Message sent!", result);

        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(redisTemplate).opsForHash().put("message:1", "id", 1L);
        verify(redisTemplate).opsForHash().put("message:1", "content", specialMessage);
        verify(redisTemplate).opsForHash().put("message:1", "user", "testUser");
        verify(redisTemplate).opsForHash().put("message:1", "timestamp", chatMessage.getTimestamp());
        verify(redisTemplate).opsForList().rightPush("chatRoomMessages", 1L);
        verify(redisTemplate).opsForZSet().add("chatRoomMessagesByTime", 1L, chatMessage.getTimestamp());
    }

    @Test
    public void testSendMessageWithNullUsername() {
        when(authentication.getName()).thenReturn(null);
        when(rateLimitingService.isAllowed(null)).thenReturn(true);

        ChatMessage chatMessage = new ChatMessage(null, "Hello");
        chatMessage.setId(1L);
        chatMessage.setTimestamp(System.currentTimeMillis());

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        String result = chatService.sendMessage("Hello");
        assertEquals("Message sent!", result);

        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(redisTemplate).opsForHash().put("message:1", "id", 1L);
        verify(redisTemplate).opsForHash().put("message:1", "content", "Hello");
        verify(redisTemplate).opsForHash().put("message:1", "user", null);
        verify(redisTemplate).opsForHash().put("message:1", "timestamp", chatMessage.getTimestamp());
        verify(redisTemplate).opsForList().rightPush("chatRoomMessages", 1L);
        verify(redisTemplate).opsForZSet().add("chatRoomMessagesByTime", 1L, chatMessage.getTimestamp());
    }

    @Test
    public void testSendMessageWithEmptyUsername() {
        when(authentication.getName()).thenReturn("");
        when(rateLimitingService.isAllowed("")).thenReturn(true);

        ChatMessage chatMessage = new ChatMessage("", "Hello");
        chatMessage.setId(1L);
        chatMessage.setTimestamp(System.currentTimeMillis());

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        String result = chatService.sendMessage("Hello");
        assertEquals("Message sent!", result);

        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(redisTemplate).opsForHash().put("message:1", "id", 1L);
        verify(redisTemplate).opsForHash().put("message:1", "content", "Hello");
        verify(redisTemplate).opsForHash().put("message:1", "user", "");
        verify(redisTemplate).opsForHash().put("message:1", "timestamp", chatMessage.getTimestamp());
        verify(redisTemplate).opsForList().rightPush("chatRoomMessages", 1L);
        verify(redisTemplate).opsForZSet().add("chatRoomMessagesByTime", 1L, chatMessage.getTimestamp());
    }

    @Test
    public void testSendMessageWithNullAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        String result = chatService.sendMessage("Hello");
        assertEquals("Error", result);

        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
        verify(redisTemplate, never()).opsForHash();
        verify(redisTemplate, never()).opsForList();
        verify(redisTemplate, never()).opsForZSet();
    }

    @Test
    public void testSendMessageWithAnonymousUser() {
        when(authentication.getName()).thenReturn("anonymousUser");
        when(rateLimitingService.isAllowed("anonymousUser")).thenReturn(true);

        ChatMessage chatMessage = new ChatMessage("anonymousUser", "Hello");
        chatMessage.setId(1L);
        chatMessage.setTimestamp(System.currentTimeMillis());

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        String result = chatService.sendMessage("Hello");
        assertEquals("Message sent!", result);

        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(redisTemplate).opsForHash().put("message:1", "id", 1L);
        verify(redisTemplate).opsForHash().put("message:1", "content", "Hello");
        verify(redisTemplate).opsForHash().put("message:1", "user", "anonymousUser");
        verify(redisTemplate).opsForHash().put("message:1", "timestamp", chatMessage.getTimestamp());
        verify(redisTemplate).opsForList().rightPush("chatRoomMessages", 1L);
        verify(redisTemplate).opsForZSet().add("chatRoomMessagesByTime", 1L, chatMessage.getTimestamp());
    }

    @Test
    public void testSendMessageWithCustomUserDetails() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("customUser");
        when(rateLimitingService.isAllowed("customUser")).thenReturn(true);

        ChatMessage chatMessage = new ChatMessage("customUser", "Hello");
        chatMessage.setId(1L);
        chatMessage.setTimestamp(System.currentTimeMillis());

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        String result = chatService.sendMessage("Hello");
        assertEquals("Message sent!", result);

        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(redisTemplate).opsForHash().put("message:1", "id", 1L);
        verify(redisTemplate).opsForHash().put("message:1", "content", "Hello");
        verify(redisTemplate).opsForHash().put("message:1", "user", "customUser");
        verify(redisTemplate).opsForHash().put("message:1", "timestamp", chatMessage.getTimestamp());
        verify(redisTemplate).opsForList().rightPush("chatRoomMessages", 1L);
        verify(redisTemplate).opsForZSet().add("chatRoomMessagesByTime", 1L, chatMessage.getTimestamp());
    }

    @Test
    public void testSendMessageWithNullUserDetails() {
        when(authentication.getPrincipal()).thenReturn(null);
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(true);

        ChatMessage chatMessage = new ChatMessage("testUser", "Hello");
        chatMessage.setId(1L);
        chatMessage.setTimestamp(System.currentTimeMillis());

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        String result = chatService.sendMessage("Hello");
        assertEquals("Message sent!", result);

        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(redisTemplate).opsForHash().put("message:1", "id", 1L);
        verify(redisTemplate).opsForHash().put("message:1", "content", "Hello");
        verify(redisTemplate).opsForHash().put("message:1", "user", "testUser");
        verify(redisTemplate).opsForHash().put("message:1", "timestamp", chatMessage.getTimestamp());
        verify(redisTemplate).opsForList().rightPush("chatRoomMessages", 1L);
        verify(redisTemplate).opsForZSet().add("chatRoomMessagesByTime", 1L, chatMessage.getTimestamp());
    }

    @Test
    public void testSendMessageWithEmptyUserDetails() {
        when(authentication.getPrincipal()).thenReturn("");
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(true);

        ChatMessage chatMessage = new ChatMessage("testUser", "Hello");
        chatMessage.setId(1L);
        chatMessage.setTimestamp(System.currentTimeMillis());

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        String result = chatService.sendMessage("Hello");
        assertEquals("Message sent!", result);

        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(redisTemplate).opsForHash().put("message:1", "id", 1L);
        verify(redisTemplate).opsForHash().put("message:1", "content", "Hello");
        verify(redisTemplate).opsForHash().put("message:1", "user", "testUser");
        verify(redisTemplate).opsForHash().put("message:1", "timestamp", chatMessage.getTimestamp());
        verify(redisTemplate).opsForList().rightPush("chatRoomMessages", 1L);
        verify(redisTemplate).opsForZSet().add("chatRoomMessagesByTime", 1L, chatMessage.getTimestamp());
    }

    @Test
    public void testSendMessageWithNullSecurityContext() {
        SecurityContextHolder.setContext(null);

        String result = chatService.sendMessage("Hello");
        assertEquals("Error", result);

        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
        verify(redisTemplate, never()).opsForHash();
        verify(redisTemplate, never()).opsForList();
        verify(redisTemplate, never()).opsForZSet();
    }

    @Test
    public void testSendMessageWithNullSecurityContextAndAuthentication() {
        SecurityContextHolder.setContext(null);
        when(securityContext.getAuthentication()).thenReturn(null);

        String result = chatService.sendMessage("Hello");
        assertEquals("Error", result);

        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
        verify(redisTemplate, never()).opsForHash();
        verify(redisTemplate, never()).opsForList();
        verify(redisTemplate, never()).opsForZSet();
    }

    @Test
    public void testSendMessageWithNullSecurityContextAndPrincipal() {
        SecurityContextHolder.setContext(null);
        when(authentication.getPrincipal()).thenReturn(null);

        String result = chatService.sendMessage("Hello");
        assertEquals("Error", result);

        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
        verify(redisTemplate, never()).opsForHash();
        verify(redisTemplate, never()).opsForList();
        verify(redisTemplate, never()).opsForZSet();
    }

    @Test
    public void testSendMessageWithNullSecurityContextAndUsername() {
        SecurityContextHolder.setContext(null);
        when(authentication.getName()).thenReturn(null);

        String result = chatService.sendMessage("Hello");
        assertEquals("Error", result);

        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
        verify(redisTemplate, never()).opsForHash();
        verify(redisTemplate, never()).opsForList();
        verify(redisTemplate, never()).opsForZSet();
    }

    @Test
    public void testSendMessageWithNullSecurityContextAndEmptyUsername() {
        SecurityContextHolder.setContext(null);
        when(authentication.getName()).thenReturn("");

        String result = chatService.sendMessage("Hello");
        assertEquals("Error", result);

        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
        verify(redisTemplate, never()).opsForHash();
        verify(redisTemplate, never()).opsForList();
        verify(redisTemplate, never()).opsForZSet();
    }

    @Test
    public void testSendMessageWithNullSecurityContextAndEmptyPrincipal() {
        SecurityContextHolder.setContext(null);
        when(authentication.getPrincipal()).thenReturn("");

        String result = chatService.sendMessage("Hello");
        assertEquals("Error", result);

        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
        verify(redisTemplate, never()).opsForHash();
        verify(redisTemplate, never()).opsForList();
        verify(redisTemplate, never()).opsForZSet();
    }
    
    @Test
    public void testGetMessagesSinceWithValidTimestamp() {
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(true);

        Set<Object> messageIds = new HashSet<>(Arrays.asList(1L, 2L));
        when(redisTemplate.opsForZSet().rangeByScore(anyString(), anyDouble(), anyDouble())).thenReturn(messageIds);

        Map<Object, Object> messageMap1 = new HashMap<>();
        messageMap1.put("id", 1L);
        messageMap1.put("user", "testUser");
        messageMap1.put("content", "Hello");
        messageMap1.put("timestamp", 123456789L);

        Map<Object, Object> messageMap2 = new HashMap<>();
        messageMap2.put("id", 2L);
        messageMap2.put("user", "testUser2");
        messageMap2.put("content", "Hi");
        messageMap2.put("timestamp", 123456790L);

        when(redisTemplate.opsForHash().entries("message:1")).thenReturn(messageMap1);
        when(redisTemplate.opsForHash().entries("message:2")).thenReturn(messageMap2);

        List<ChatMessage> messages = chatService.getMessagesSince(123456788L);

        assertEquals(2, messages.size());
        assertEquals("Hello", messages.get(0).getMessage());
        assertEquals("Hi", messages.get(1).getMessage());
    }

    @Test
    public void testGetMessagesSinceWithEmptyCache() {
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(true);

        when(redisTemplate.opsForZSet().rangeByScore(anyString(), anyDouble(), anyDouble())).thenReturn(Collections.emptySet());

        List<ChatMessage> dbMessages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(1L);
        chatMessage.setUsername("testUser");
        chatMessage.setMessage("Hello from DB");
        chatMessage.setTimestamp(123456789L);
        dbMessages.add(chatMessage);

        when(chatMessageRepository.findAllByTimestampGreaterThan(anyLong())).thenReturn(dbMessages);

        List<ChatMessage> messages = chatService.getMessagesSince(123456788L);

        assertEquals(1, messages.size());
        assertEquals("Hello from DB", messages.get(0).getMessage());
    }

    @Test
    public void testGetMessagesSinceWithRateLimitExceeded() {
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(false);

        List<ChatMessage> messages = chatService.getMessagesSince(123456788L);

        assertEquals(true, messages.isEmpty());
    }

    @Test
    public void testGetMessagesSinceWithException() {
        when(authentication.getName()).thenReturn("testUser");
        when(rateLimitingService.isAllowed("testUser")).thenReturn(true);

        when(redisTemplate.opsForZSet().rangeByScore(anyString(), anyDouble(), anyDouble())).thenThrow(new RuntimeException("Test Exception"));

        List<ChatMessage> messages = chatService.getMessagesSince(123456788L);

        assertEquals(true, messages.isEmpty());
    }

    @Test
    public void testGetMessagesSinceWithNullAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        List<ChatMessage> messages = chatService.getMessagesSince(123456788L);

        assertEquals(true, messages.isEmpty());
    }
    
        @Test
    public void testDeleteMessage_Success() {
        long messageId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);
        when(chatMessageRepository.existsById(messageId)).thenReturn(true);

        String result = chatService.deleteMessage(messageId);

        verify(redisTemplate).opsForHash().delete("message:" + messageId);
        verify(redisTemplate).opsForList().remove("chatRoomMessages", 1, messageId);
        verify(redisTemplate).opsForZSet().remove("chatRoomMessagesByTime", messageId);
        verify(chatMessageRepository).deleteById(messageId);

        assertEquals("Successfully deleted the message", result);
    }

    @Test
    public void testDeleteMessage_MessageNotFound() {
        long messageId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);
        when(chatMessageRepository.existsById(messageId)).thenReturn(false);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Message not found", result);
    }

    @Test
    public void testDeleteMessage_RateLimitExceeded() {
        long messageId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(false);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Rate limit exceeded. Please try again later.", result);
    }

    @Test
    public void testDeleteMessage_ExceptionHandling() {
        long messageId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);
        when(chatMessageRepository.existsById(messageId)).thenThrow(new RuntimeException("Test Exception"));

        String result = chatService.deleteMessage(messageId);

        assertEquals("Error", result);
    }

    @Test
    public void testDeleteMessage_NullUsername() {
        long messageId = 1L;

        when(authentication.getName()).thenReturn(null);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Rate limit exceeded. Please try again later.", result);
    }

    @Test
    public void testDeleteMessage_EmptyUsername() {
        long messageId = 1L;

        when(authentication.getName()).thenReturn("");

        String result = chatService.deleteMessage(messageId);

        assertEquals("Rate limit exceeded. Please try again later.", result);
    }

    @Test
    public void testDeleteMessage_NullAuthentication() {
        long messageId = 1L;

        when(securityContext.getAuthentication()).thenReturn(null);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Rate limit exceeded. Please try again later.", result);
    }

    @Test
    public void testDeleteMessage_NullSecurityContext() {
        long messageId = 1L;

        SecurityContextHolder.setContext(null);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Rate limit exceeded. Please try again later.", result);
    }

    @Test
    public void testDeleteMessage_NullMessageId() {
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);

        String result = chatService.deleteMessage(0L);

        assertEquals("Message not found", result);
    }

    @Test
    public void testDeleteMessage_NegativeMessageId() {
        long messageId = -1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);
        when(chatMessageRepository.existsById(messageId)).thenReturn(false);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Message not found", result);
    }

    @Test
    public void testDeleteMessage_ZeroMessageId() {
        long messageId = 0L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);
        when(chatMessageRepository.existsById(messageId)).thenReturn(false);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Message not found", result);
    }

    @Test
    public void testDeleteMessage_ValidMessageId() {
        long messageId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);
        when(chatMessageRepository.existsById(messageId)).thenReturn(true);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Successfully deleted the message", result);
    }

    @Test
    public void testDeleteMessage_InvalidMessageId() {
        long messageId = 999L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);
        when(chatMessageRepository.existsById(messageId)).thenReturn(false);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Message not found", result);
    }

    @Test
    public void testDeleteMessage_ValidUsername() {
        long messageId = 1L;
        String username = "validUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);
        when(chatMessageRepository.existsById(messageId)).thenReturn(true);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Successfully deleted the message", result);
    }

    @Test
    public void testDeleteMessage_InvalidUsername() {
        long messageId = 1L;
        String username = "invalidUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(false);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Rate limit exceeded. Please try again later.", result);
    }

    @Test
    public void testDeleteMessage_EmptyMessageId() {
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);

        String result = chatService.deleteMessage(0L);

        assertEquals("Message not found", result);
    }

    @Test
    public void testDeleteMessage_EmptyMessage() {
        long messageId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);
        when(chatMessageRepository.existsById(messageId)).thenReturn(true);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Successfully deleted the message", result);
    }

    @Test
    public void testDeleteMessage_ValidMessage() {
        long messageId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);
        when(chatMessageRepository.existsById(messageId)).thenReturn(true);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Successfully deleted the message", result);
    }

    @Test
    public void testDeleteMessage_InvalidMessage() {
        long messageId = 999L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(rateLimitingService.isAllowed(username)).thenReturn(true);
        when(chatMessageRepository.existsById(messageId)).thenReturn(false);

        String result = chatService.deleteMessage(messageId);

        assertEquals("Message not found", result);
    }
}
