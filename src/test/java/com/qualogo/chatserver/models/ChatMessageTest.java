package com.qualogo.chatserver.models;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ChatMessageTest {

    @Test
    public void testChatMessageIdIsSetAndRetrievedCorrectly() {
        long expectedId = 1L;
        ChatMessage chatMessage = new ChatMessage(expectedId, "username", "message", System.currentTimeMillis());
        assertEquals(expectedId, chatMessage.getId());
    }

    @Test
    public void testChatMessageIdIsSetAndRetrievedCorrectlyWithDifferentId() {
        long expectedId = 2L;
        ChatMessage chatMessage = new ChatMessage(expectedId, "username", "message", System.currentTimeMillis());
        assertEquals(expectedId, chatMessage.getId());
    }

    @Test
    public void testChatMessageIdIsSetAndRetrievedCorrectlyWithNegativeId() {
        long expectedId = -1L;
        ChatMessage chatMessage = new ChatMessage(expectedId, "username", "message", System.currentTimeMillis());
        assertEquals(expectedId, chatMessage.getId());
    }

    @Test
    public void testChatMessageIdIsSetAndRetrievedCorrectlyWithZeroId() {
        long expectedId = 0L;
        ChatMessage chatMessage = new ChatMessage(expectedId, "username", "message", System.currentTimeMillis());
        assertEquals(expectedId, chatMessage.getId());
    }

    @Test
    public void testChatMessageIdIsSetAndRetrievedCorrectlyWithMaxLongId() {
        long expectedId = Long.MAX_VALUE;
        ChatMessage chatMessage = new ChatMessage(expectedId, "username", "message", System.currentTimeMillis());
        assertEquals(expectedId, chatMessage.getId());
    }
}