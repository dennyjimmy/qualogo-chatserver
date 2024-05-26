package com.qualogo.chatserver.models;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testuser", "testuser@example.com", "password123");
    }

    @Test
    public void testUserConstructor() {
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("testuser@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testSetAndGetId() {
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    public void testSetAndGetUsername() {
        user.setUsername("newusername");
        assertEquals("newusername", user.getUsername());
    }

    @Test
    public void testSetAndGetEmail() {
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test
    public void testSetAndGetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    public void testSetAndGetRoles() {
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        roles.add(role);
        user.setRoles(roles);
        assertEquals(1, user.getRoles().size());
        assertEquals(ERole.ROLE_USER, user.getRoles().iterator().next().getName());
    }
}