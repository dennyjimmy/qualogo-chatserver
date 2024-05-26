package com.qualogo.chatserver.repository;

import com.qualogo.chatserver.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByTimestampGreaterThan(long timestamp);
}