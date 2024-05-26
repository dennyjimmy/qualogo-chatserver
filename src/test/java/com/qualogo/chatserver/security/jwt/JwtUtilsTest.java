package com.qualogo.chatserver.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.qualogo.chatserver.security.services.UserDetailsImpl;

import io.jsonwebtoken.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        jwtUtils.setJwtSecret("testSecretKey12345678901234567890123456789012");
        jwtUtils.setJwtExpirationMs(3600000); // 1 hour
    }

    @Test
    public void testGenerateJwtToken() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("testUser", jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        String token = generateTestToken("testUser");

        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("testUser", username);
    }

    @Test
    public void testValidateJwtToken_ValidToken() {
        String token = generateTestToken("testUser");

        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void testValidateJwtToken_InvalidToken() {
        String invalidToken = "invalidToken";

        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    public void testValidateJwtToken_ExpiredToken() {
        String expiredToken = generateExpiredTestToken("testUser");

        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

    @Test
    public void testValidateJwtToken_UnsupportedToken() {
        String unsupportedToken = Jwts.builder()
                .setSubject("testUser")
                .signWith(jwtUtils.key(), SignatureAlgorithm.HS512) // Unsupported algorithm
                .compact();

        assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
    }

    @Test
    public void testValidateJwtToken_EmptyClaims() {
        String emptyClaimsToken = Jwts.builder()
                .setSubject("")
                .signWith(jwtUtils.key(), SignatureAlgorithm.HS256)
                .compact();

        assertFalse(jwtUtils.validateJwtToken(emptyClaimsToken));
    }

    @Test
    public void testKey() {
        Key key = jwtUtils.key();

        assertNotNull(key);
    }

    @Test
    public void testGetJwtSecret() {
        assertEquals("testSecretKey12345678901234567890123456789012", jwtUtils.getJwtSecret());
    }

    @Test
    public void testSetJwtSecret() {
        jwtUtils.setJwtSecret("newSecretKey");
        assertEquals("newSecretKey", jwtUtils.getJwtSecret());
    }

    @Test
    public void testGetJwtExpirationMs() {
        assertEquals(3600000, jwtUtils.getJwtExpirationMs());
    }

    @Test
    public void testSetJwtExpirationMs() {
        jwtUtils.setJwtExpirationMs(7200000); // 2 hours
        assertEquals(7200000, jwtUtils.getJwtExpirationMs());
    }

    private String generateTestToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtUtils.getJwtExpirationMs()))
                .signWith(jwtUtils.key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateExpiredTestToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 3600000)) // 1 hour ago
                .setExpiration(new Date(System.currentTimeMillis() - 1800000)) // 30 minutes ago
                .signWith(jwtUtils.key(), SignatureAlgorithm.HS256)
                .compact();
    }
}