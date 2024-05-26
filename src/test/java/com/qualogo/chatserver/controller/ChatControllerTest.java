package com.qualogo.chatserver.controller;

import com.qualogo.chatserver.controllers.ChatController;
import com.qualogo.chatserver.models.ChatMessage;
import com.qualogo.chatserver.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ChatControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
    }

    @Test
    public void testSendMessage() throws Exception {
        String message = "Hello, World!";
        when(chatService.sendMessage(anyString())).thenReturn("Message sent successfully");

        mockMvc.perform(post("/api/chat/send")
                .param("message", message)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Message sent successfully"));
    }

    @Test
    public void testReceiveMessages() throws Exception {
        long timestamp = 1620000000000L;
        List<ChatMessage> messages = Arrays.asList(
                new ChatMessage(1L, "User1", "Hello", timestamp),
                new ChatMessage(2L, "User2", "Hi", timestamp + 1000)
        );
        when(chatService.getMessagesSince(anyLong())).thenReturn(messages);

        mockMvc.perform(get("/api/chat/receive")
                .param("timestamp", String.valueOf(timestamp))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("User1"))
                .andExpect(jsonPath("$[0].message").value("Hello"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].username").value("User2"))
                .andExpect(jsonPath("$[1].message").value("Hi"));
    }

    @Test
    public void testDeleteMessage() throws Exception {
        long messageId = 1L;
        when(chatService.deleteMessage(anyLong())).thenReturn("Message deleted successfully");

        mockMvc.perform(delete("/api/chat/deleteMsg")
                .param("messageId", String.valueOf(messageId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Message deleted successfully"));
    }
}