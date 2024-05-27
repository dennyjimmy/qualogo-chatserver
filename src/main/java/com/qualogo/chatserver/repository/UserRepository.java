package com.qualogo.chatserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qualogo.chatserver.models.User;

/**
 * Repository interface for User entity.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User by their username.
     *
     * @param username the username of the User to find.
     * @return an Optional containing the found User, or empty if no User found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a User exists by their username.
     *
     * @param username the username to check for existence.
     * @return true if a User with the given username exists, false otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a User exists by their email.
     *
     * @param email the email to check for existence.
     * @return true if a User with the given email exists, false otherwise.
     */
    Boolean existsByEmail(String email);
}