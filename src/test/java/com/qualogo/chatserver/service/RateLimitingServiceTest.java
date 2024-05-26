package com.qualogo.chatserver.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RateLimitingServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private RateLimitingService rateLimitingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        rateLimitingService.setMaxRequests(5);
        rateLimitingService.setTimeWindow(60);
    }

    @Test
    public void testIsAllowed_FirstRequest() {
        String userId = "user1";
        when(valueOperations.increment("rateLimit:" + userId, 1)).thenReturn(1L);

        boolean result = rateLimitingService.isAllowed(userId);

        assertTrue(result);
        verify(redisTemplate).expire("rateLimit:" + userId, 60, TimeUnit.SECONDS);
    }

    @Test
    public void testIsAllowed_WithinRateLimit() {
        String userId = "user1";
        when(valueOperations.increment("rateLimit:" + userId, 1)).thenReturn(3L);

        boolean result = rateLimitingService.isAllowed(userId);

        assertTrue(result);
        verify(redisTemplate, never()).expire(anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    public void testIsAllowed_ExceedsRateLimit() {
        String userId = "user1";
        when(valueOperations.increment("rateLimit:" + userId, 1)).thenReturn(6L);

        boolean result = rateLimitingService.isAllowed(userId);

        assertFalse(result);
        verify(redisTemplate, never()).expire(anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    public void testIsAllowed_IncrementFails() {
        String userId = "user1";
        when(valueOperations.increment("rateLimit:" + userId, 1)).thenReturn(null);

        boolean result = rateLimitingService.isAllowed(userId);

        assertFalse(result);
        verify(redisTemplate, never()).expire(anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    public void testIsAllowed_ExpireFails() {
        String userId = "user1";
        when(valueOperations.increment("rateLimit:" + userId, 1)).thenReturn(1L);
        doThrow(new RuntimeException("Expire failed")).when(redisTemplate).expire("rateLimit:" + userId, 60, TimeUnit.SECONDS);

        boolean result = rateLimitingService.isAllowed(userId);

        assertTrue(result); // The method should still return true as the increment was successful
        verify(redisTemplate).expire("rateLimit:" + userId, 60, TimeUnit.SECONDS);
    }
}