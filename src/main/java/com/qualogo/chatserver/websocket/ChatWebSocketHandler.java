package com.qualogo.chatserver.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Broadcast the message to all connected clients
        /*for (WebSocketSession webSocketSession : session.getOpenSessions()) {
            webSocketSession.sendMessage(message);
        }*/
    }
}