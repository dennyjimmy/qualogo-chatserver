package com.qualogo.chatserver.models;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents a chat message entity.
 */
@Entity
@Schema(description = "Chat message entity")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the message", example = "1")
    private long id;

    @Schema(description = "Username of the sender", example = "thien_tran")
    private String username;
    
    @Schema(description = "Content of the message", example = "Hello, everyone!")
    private String message;
    
    @Schema(description = "Timestamp of when the message was sent", example = "1622548800000")
    private long timestamp;

    /**
     * Default constructor for JPA.
     */
    public ChatMessage() {}

    /**
     * Constructs a new ChatMessage with the specified username and message.
     * The timestamp is set to the current system time.
     *
     * @param username the username of the sender
     * @param message the content of the message
     */
    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Constructs a new ChatMessage with the specified id, username, message, and timestamp.
     *
     * @param id the unique identifier of the message
     * @param username the username of the sender
     * @param message the content of the message
     * @param timestamp the timestamp of when the message was sent
     */
    public ChatMessage(long id, String username, String message, long timestamp) {
        this.id = id;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Returns the unique identifier of the message.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the message.
     *
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the username of the sender.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the sender.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the content of the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the content of the message.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the timestamp of when the message was sent.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of when the message was sent.
     *
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}