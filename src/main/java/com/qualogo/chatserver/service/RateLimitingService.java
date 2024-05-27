package com.qualogo.chatserver.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for rate limiting user requests using Redis.
 */
@Service
public class RateLimitingService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${rate.limit.maxRequests}")
    private int maxRequests;

    @Value("${rate.limit.timeWindow}")
    private int timeWindow;

    /**
     * Checks if a user is allowed to make a request based on the rate limit.
     *
     * @param userId the ID of the user making the request
     * @return true if the user is allowed to make the request, false otherwise
     */
    public boolean isAllowed(String userId) {
        String key = "rateLimit:" + userId;
        Long currentCount = redisTemplate.opsForValue().increment(key, 1);
        if (currentCount == 1) {
            redisTemplate.expire(key, getTimeWindow(), TimeUnit.SECONDS);
        }
        return currentCount <= getMaxRequests();
    }

    /**
     * Gets the maximum number of requests allowed within the time window.
     *
     * @return the maximum number of requests
     */
    public int getMaxRequests() {
        return maxRequests;
    }

    /**
     * Sets the maximum number of requests allowed within the time window.
     *
     * @param maxRequests the maximum number of requests to set
     */
    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    /**
     * Gets the time window in seconds for rate limiting.
     *
     * @return the time window in seconds
     */
    public int getTimeWindow() {
        return timeWindow;
    }

    /**
     * Sets the time window in seconds for rate limiting.
     *
     * @param timeWindow the time window in seconds to set
     */
    public void setTimeWindow(int timeWindow) {
        this.timeWindow = timeWindow;
    }
}