package com.qualogo.chatserver;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
public class ChatServerApplicationTest {

    @Test
    public void contextLoads() {
        // This test will simply start the application context to ensure it loads without issues.
        ConfigurableApplicationContext context = SpringApplication.run(ChatServerApplication.class);
        assertNotNull(context);
        context.close();
    }

    @Test
    public void testMainMethod() {
        // Mocking SpringApplication to verify that run method is called with correct arguments
        SpringApplication mockSpringApplication = mock(SpringApplication.class);
        SpringApplication.run(ChatServerApplication.class, new String[]{});
        verify(mockSpringApplication, Mockito.times(1)).run(any(Class.class), any(String[].class));
    }

    @Test
    public void testMainMethodWithArgs() {
        // Mocking SpringApplication to verify that run method is called with correct arguments
        SpringApplication mockSpringApplication = mock(SpringApplication.class);
        String[] args = {"arg1", "arg2"};
        SpringApplication.run(ChatServerApplication.class, args);
        verify(mockSpringApplication, Mockito.times(1)).run(any(Class.class), any(String[].class));
    }

    @Test
    public void testApplicationContext() {
        // This test will start the application context and ensure it loads without issues.
        ConfigurableApplicationContext context = SpringApplication.run(ChatServerApplication.class);
        assertNotNull(context);
        context.close();
    }

    @Test
    public void testApplicationContextWithArgs() {
        // This test will start the application context with arguments and ensure it loads without issues.
        String[] args = {"arg1", "arg2"};
        ConfigurableApplicationContext context = SpringApplication.run(ChatServerApplication.class, args);
        assertNotNull(context);
        context.close();
    }
}