package com.qualogo.chatserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qualogo.chatserver.models.ERole;
import com.qualogo.chatserver.models.Role;

/**
 * Repository interface for {@link Role} entities.
 * This interface extends {@link JpaRepository} to provide CRUD operations for Role entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a {@link Role} by its name.
     *
     * @param name the name of the role to find, represented by {@link ERole}.
     * @return an {@link Optional} containing the found {@link Role}, or an empty {@link Optional} if no role was found.
     */
    Optional<Role> findByName(ERole name);
}