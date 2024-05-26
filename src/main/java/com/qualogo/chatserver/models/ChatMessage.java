package com.qualogo.chatserver.models;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

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

    public ChatMessage() {}

    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
    public ChatMessage(long id, String username, String message, long timestamp) {
        this.id = id;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and Setters

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}