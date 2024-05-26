package com.qualogo.chatserver.payload.response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class JwtResponseTest {

    @Test
    public void testConstructorAndGetters() {
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        JwtResponse jwtResponse = new JwtResponse("testToken", 1L, "testUser", "test@example.com", roles);

        assertEquals("testToken", jwtResponse.getAccessToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("testUser", jwtResponse.getUsername());
        assertEquals("test@example.com", jwtResponse.getEmail());
        assertEquals(roles, jwtResponse.getRoles());
        assertEquals("Bearer", jwtResponse.getTokenType());
    }

    @Test
    public void testSetAccessToken() {
        JwtResponse jwtResponse = new JwtResponse("testToken", 1L, "testUser", "test@example.com", Arrays.asList("ROLE_USER"));
        jwtResponse.setAccessToken("newToken");

        assertEquals("newToken", jwtResponse.getAccessToken());
    }

    @Test
    public void testSetTokenType() {
        JwtResponse jwtResponse = new JwtResponse("testToken", 1L, "testUser", "test@example.com", Arrays.asList("ROLE_USER"));
        jwtResponse.setTokenType("newType");

        assertEquals("newType", jwtResponse.getTokenType());
    }

    @Test
    public void testSetId() {
        JwtResponse jwtResponse = new JwtResponse("testToken", 1L, "testUser", "test@example.com", Arrays.asList("ROLE_USER"));
        jwtResponse.setId(2L);

        assertEquals(2L, jwtResponse.getId());
    }

    @Test
    public void testSetEmail() {
        JwtResponse jwtResponse = new JwtResponse("testToken", 1L, "testUser", "test@example.com", Arrays.asList("ROLE_USER"));
        jwtResponse.setEmail("new@example.com");

        assertEquals("new@example.com", jwtResponse.getEmail());
    }
}