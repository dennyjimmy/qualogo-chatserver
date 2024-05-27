package com.qualogo.chatserver.repository;

import com.qualogo.chatserver.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link ChatMessage} entities.
 * Extends {@link JpaRepository} to provide CRUD operations and custom query methods.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * Retrieves all chat messages with a timestamp greater than the specified value.
     *
     * @param timestamp the timestamp to compare against.
     * @return a list of {@link ChatMessage} objects with timestamps greater than the specified value.
     */
    List<ChatMessage> findAllByTimestampGreaterThan(long timestamp);
}