package com.qualogo.chatserver.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RateLimitingService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${rate.limit.maxRequests}")
    private int maxRequests;

    @Value("${rate.limit.timeWindow}")
    private int timeWindow;

    public boolean isAllowed(String userId) {
        String key = "rateLimit:" + userId;
        Long currentCount = redisTemplate.opsForValue().increment(key, 1);
        if (currentCount == 1) {
            redisTemplate.expire(key, timeWindow, TimeUnit.SECONDS);
        }
        return currentCount <= maxRequests;
    }
}