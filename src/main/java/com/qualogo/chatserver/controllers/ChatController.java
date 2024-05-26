package com.qualogo.chatserver.controller;

import com.qualogo.chatserver.model.ChatMessage;
import com.qualogo.chatserver.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Operation(summary = "Join a chat room", description = "Enables users to join the chat room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully joined the chat room",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/joinRoom")
    public String joinRoom() {
        return chatService.joinRoom();
    }

    @Operation(summary = "Send a message", description = "Sends a message from a user to the chat room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully sent the message",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/send")
    public String sendMessage(@Parameter(description = "The message to be sent", required = true)
            @RequestParam String message) {
        return chatService.sendMessage(message);
    }

    @Operation(summary = "Retrieve messages", description = "Retrieves the history of the chat room from the given timestamp")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved messages",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatMessage.class, type = "array")))
    })
    @GetMapping("/receive")
    public List<ChatMessage> receiveMessages(@Parameter(description = "The timestamp from which to retrieve messages (in milliseconds)", required = true)
            @RequestParam long timestamp) {
        return chatService.getMessagesSince(timestamp);
    }

    @Operation(summary = "Delete a message", description = "Deletes a message in the chat room by message ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted the message",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/deleteMsg")
    public String deleteMessage(@Parameter(description = "The ID of the message to be deleted", required = true)
            @RequestParam long messageId) {
        return chatService.deleteMessage(messageId);
    }
}
