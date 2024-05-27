package com.qualogo.chatserver.payload.response;

/**
 * The MessageResponse class is a simple POJO (Plain Old Java Object) that represents
 * a response message. It contains a single field, 'message', which holds the response
 * message string.
 */
public class MessageResponse {

    private String message;

    /**
     * Constructs a new MessageResponse with the specified message.
     *
     * @param message the message to be set in the response
     */
    public MessageResponse(String message) {
        this.message = message;
    }

    /**
     * Returns the message contained in this response.
     *
     * @return the message string
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message for this response.
     *
     * @param message the message string to be set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}