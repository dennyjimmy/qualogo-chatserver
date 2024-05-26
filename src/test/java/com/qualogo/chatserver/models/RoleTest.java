package com.qualogo.chatserver.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    public void testNoArgsConstructor() {
        Role role = new Role();
        assertNull(role.getId());
        assertNull(role.getName());
    }

    @Test
    public void testAllArgsConstructor() {
        ERole roleName = ERole.ROLE_USER;
        Role role = new Role(roleName);
        assertNull(role.getId());
        assertEquals(roleName, role.getName());
    }

    @Test
    public void testSetId() {
        Role role = new Role();
        role.setId(1);
        assertEquals(1, role.getId());
    }

    @Test
    public void testSetName() {
        Role role = new Role();
        ERole roleName = ERole.ROLE_ADMIN;
        role.setName(roleName);
        assertEquals(roleName, role.getName());
    }

    @Test
    public void testGetId() {
        Role role = new Role();
        role.setId(2);
        assertEquals(2, role.getId());
    }

    @Test
    public void testGetName() {
        ERole roleName = ERole.ROLE_MODERATOR;
        Role role = new Role(roleName);
        assertEquals(roleName, role.getName());
    }
}