package com.qualogo.chatserver.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private RedisTemplate<String, String> stringRedisTemplate;

    @Mock
    private RedisConnectionFactory redisConnectionFactory;

    @InjectMocks
    private RedisConfigTest redisConfigTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRedisTemplateBeanExists() {
        RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);
        assertNotNull(redisTemplate, "RedisTemplate bean should be configured");
    }

    @Test
    public void testStringRedisTemplateBeanExists() {
        RedisTemplate<String, String> stringRedisTemplate = applicationContext.getBean("stringRedisTemplate", RedisTemplate.class);
        assertNotNull(stringRedisTemplate, "StringRedisTemplate bean should be configured");
    }

    @Test
    public void testRedisTemplateKeySerializer() {
        RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);
        assertTrue(redisTemplate.getKeySerializer() instanceof StringRedisSerializer, "Key serializer should be StringRedisSerializer");
    }

    @Test
    public void testRedisTemplateValueSerializer() {
        RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);
        assertTrue(redisTemplate.getValueSerializer() instanceof GenericJackson2JsonRedisSerializer, "Value serializer should be GenericJackson2JsonRedisSerializer");
    }

    @Test
    public void testRedisTemplateConnectionFactory() {
        RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);
        assertNotNull(redisTemplate.getConnectionFactory(), "Connection factory should be set");
        assertTrue(redisTemplate.getConnectionFactory() instanceof RedisConnectionFactory, "Connection factory should be an instance of RedisConnectionFactory");
    }

    @Test
    public void testRedisTemplateIsSingleton() {
        RedisTemplate<String, Object> redisTemplate1 = applicationContext.getBean("redisTemplate", RedisTemplate.class);
        RedisTemplate<String, Object> redisTemplate2 = applicationContext.getBean("redisTemplate", RedisTemplate.class);
        assertTrue(redisTemplate1 == redisTemplate2, "RedisTemplate bean should be a singleton");
    }

    @Test
    public void testRedisTemplateWithNullValues() {
        RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);
        assertNotNull(redisTemplate, "RedisTemplate bean should be configured");
        assertTrue(redisTemplate.getKeySerializer() instanceof StringRedisSerializer, "Key serializer should be StringRedisSerializer");
        assertTrue(redisTemplate.getValueSerializer() instanceof GenericJackson2JsonRedisSerializer, "Value serializer should be GenericJackson2JsonRedisSerializer");
    }
}