package com.qualogo.chatserver.security.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.qualogo.chatserver.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generates a JWT token based on the authenticated user's details.
     *
     * @param authentication the authentication object containing the user's details
     * @return a JWT token as a String
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + getJwtExpirationMs()))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a Key object from the JWT secret.
     *
     * @return a Key object used for signing the JWT
     */
    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(getJwtSecret()));
    }

    /**
     * Extracts the username from a given JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validates a given JWT token.
     *
     * @param authToken the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Gets the JWT secret.
     *
     * @return the JWT secret as a String
     */
    public String getJwtSecret() {
        return jwtSecret;
    }

    /**
     * Sets the JWT secret.
     *
     * @param jwtSecret the JWT secret to set
     */
    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    /**
     * Gets the JWT expiration time in milliseconds.
     *
     * @return the JWT expiration time in milliseconds
     */
    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    /**
     * Sets the JWT expiration time in milliseconds.
     *
     * @param jwtExpirationMs the JWT expiration time to set
     */
    public void setJwtExpirationMs(int jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
    }
}