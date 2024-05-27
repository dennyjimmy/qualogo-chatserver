package com.qualogo.chatserver.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;

/**
 * Represents a request for user signup.
 * This class contains the necessary information for a user to sign up,
 * including username, email, password, and roles.
 */
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    /**
     * Gets the username of the user.
     * 
     * @return the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     * 
     * @param username the username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email of the user.
     * 
     * @return the email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     * 
     * @param email the email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     * 
     * @return the password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * 
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the roles of the user.
     * 
     * @return a set of roles assigned to the user.
     */
    public Set<String> getRole() {
        return this.role;
    }

    /**
     * Sets the roles of the user.
     * 
     * @param role a set of roles to assign to the user.
     */
    public void setRole(Set<String> role) {
        this.role = role;
    }
}