package com.qualogo.chatserver.payload.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class MessageResponseTest {

    @Test
    public void testConstructor() {
        String testMessage = "Hello, World!";
        MessageResponse messageResponse = new MessageResponse(testMessage);
        assertEquals(testMessage, messageResponse.getMessage());
    }

    @Test
    public void testGetMessage() {
        String testMessage = "Test Message";
        MessageResponse messageResponse = new MessageResponse(testMessage);
        assertEquals(testMessage, messageResponse.getMessage());
    }

    @Test
    public void testSetMessage() {
        String initialMessage = "Initial Message";
        String newMessage = "New Message";
        MessageResponse messageResponse = new MessageResponse(initialMessage);
        messageResponse.setMessage(newMessage);
        assertEquals(newMessage, messageResponse.getMessage());
    }

    @Test
    public void testEmptyMessage() {
        String emptyMessage = "";
        MessageResponse messageResponse = new MessageResponse(emptyMessage);
        assertEquals(emptyMessage, messageResponse.getMessage());
    }

    @Test
    public void testNullMessage() {
        MessageResponse messageResponse = new MessageResponse(null);
        assertEquals(null, messageResponse.getMessage());
    }
}