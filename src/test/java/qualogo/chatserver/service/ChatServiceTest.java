package com.qualogo.chatserver.service;
import com.qualogo.chatserver.model.ChatMessage;
import com.qualogo.chatserver.repository.ChatMessageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ChatServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private ChatService chatService;

    public ChatServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMessagesSince() {
        List<ChatMessage> mockMessages = new ArrayList<>();
        mockMessages.add(new ChatMessage("user1", "Hello!"));
        when(chatMessageRepository.findAllByTimestampGreaterThan(1000L)).thenReturn(mockMessages);

        List<ChatMessage> messages = chatService.getMessagesSince(1000L);
        assertEquals(1, messages.size());
        assertEquals("Hello!", messages.get(0).getMessage());
    }
}