package com.qualogo.chatserver.repository;
import com.qualogo.chatserver.models.ChatMessage;
import com.qualogo.chatserver.repository.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatMessageRepositoryTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    private List<ChatMessage> mockMessages;

    @BeforeEach
    public void setUp() {
        // Initialize mock data
        ChatMessage message1 = new ChatMessage();
        message1.setId(1L);
        message1.setTimestamp(1000L);
        message1.setMessage("Message 1");

        ChatMessage message2 = new ChatMessage();
        message2.setId(2L);
        message2.setTimestamp(2000L);
        message2.setMessage("Message 2");

        mockMessages = Arrays.asList(message1, message2);
    }

    @Test
    public void testFindAllByTimestampGreaterThan() {
        long timestamp = 1500L;
        List<ChatMessage> expectedMessages = Arrays.asList(mockMessages.get(1));

        when(chatMessageRepository.findAllByTimestampGreaterThan(timestamp)).thenReturn(expectedMessages);

        List<ChatMessage> actualMessages = chatMessageRepository.findAllByTimestampGreaterThan(timestamp);

        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    public void testFindAllByTimestampGreaterThan_NoMessages() {
        long timestamp = 2500L;
        List<ChatMessage> expectedMessages = Arrays.asList();

        when(chatMessageRepository.findAllByTimestampGreaterThan(timestamp)).thenReturn(expectedMessages);

        List<ChatMessage> actualMessages = chatMessageRepository.findAllByTimestampGreaterThan(timestamp);

        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    public void testFindAllByTimestampGreaterThan_AllMessages() {
        long timestamp = 500L;
        List<ChatMessage> expectedMessages = mockMessages;

        when(chatMessageRepository.findAllByTimestampGreaterThan(timestamp)).thenReturn(expectedMessages);

        List<ChatMessage> actualMessages = chatMessageRepository.findAllByTimestampGreaterThan(timestamp);

        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    public void testFindAllByTimestampGreaterThan_ExactTimestamp() {
        long timestamp = 1000L;
        List<ChatMessage> expectedMessages = Arrays.asList(mockMessages.get(1));

        when(chatMessageRepository.findAllByTimestampGreaterThan(timestamp)).thenReturn(expectedMessages);

        List<ChatMessage> actualMessages = chatMessageRepository.findAllByTimestampGreaterThan(timestamp);

        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    public void testFindAllByTimestampGreaterThan_EmptyRepository() {
        long timestamp = 1000L;
        List<ChatMessage> expectedMessages = Arrays.asList();

        when(chatMessageRepository.findAllByTimestampGreaterThan(timestamp)).thenReturn(expectedMessages);

        List<ChatMessage> actualMessages = chatMessageRepository.findAllByTimestampGreaterThan(timestamp);

        assertEquals(expectedMessages, actualMessages);
    }
}