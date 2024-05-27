package com.qualogo.chatserver.payload.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a login request payload.
 * This class is used to capture the username and password
 * provided by a user attempting to log in.
 */
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    /**
     * Gets the username provided in the login request.
     *
     * @return the username as a String.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the login request.
     *
     * @param username the username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password provided in the login request.
     *
     * @return the password as a String.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the login request.
     *
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}