package com.qualogo.chatserver.payload.response;

import java.util.List;

/**
 * JwtResponse is a class that represents the response containing the JWT token and user details.
 */
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    /**
     * Constructs a new JwtResponse with the specified details.
     *
     * @param accessToken the JWT token
     * @param id the user ID
     * @param username the username
     * @param email the email address
     * @param roles the list of roles assigned to the user
     */
    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    /**
     * Returns the JWT token.
     *
     * @return the JWT token
     */
    public String getAccessToken() {
        return token;
    }

    /**
     * Sets the JWT token.
     *
     * @param accessToken the new JWT token
     */
    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    /**
     * Returns the token type.
     *
     * @return the token type
     */
    public String getTokenType() {
        return type;
    }

    /**
     * Sets the token type.
     *
     * @param tokenType the new token type
     */
    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user ID.
     *
     * @param id the new user ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the list of roles assigned to the user.
     *
     * @return the list of roles
     */
    public List<String> getRoles() {
        return roles;
    }
}