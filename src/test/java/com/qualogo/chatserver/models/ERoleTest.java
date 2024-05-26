package com.qualogo.chatserver.models;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ERoleTest {

    @Test
    public void testRoleUserExists() {
        assertNotNull(ERole.valueOf("ROLE_USER"));
    }

    @Test
    public void testRoleModeratorExists() {
        assertNotNull(ERole.valueOf("ROLE_MODERATOR"));
    }

    @Test
    public void testRoleAdminExists() {
        assertNotNull(ERole.valueOf("ROLE_ADMIN"));
    }

    @Test
    public void testRoleUserValue() {
        assertEquals(ERole.ROLE_USER, ERole.valueOf("ROLE_USER"));
    }

    @Test
    public void testRoleModeratorValue() {
        assertEquals(ERole.ROLE_MODERATOR, ERole.valueOf("ROLE_MODERATOR"));
    }

    @Test
    public void testRoleAdminValue() {
        assertEquals(ERole.ROLE_ADMIN, ERole.valueOf("ROLE_ADMIN"));
    }
}