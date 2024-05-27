package com.qualogo.chatserver.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.qualogo.chatserver.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Implementation of the UserDetails interface, which provides core user information.
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Default constructor.
     */
    public UserDetailsImpl(){
    }
    
    /**
     * Parameterized constructor to initialize UserDetailsImpl.
     *
     * @param id the user's ID
     * @param username the user's username
     * @param email the user's email
     * @param password the user's password
     * @param authorities the authorities granted to the user
     */
    public UserDetailsImpl(Long id, String username, String email, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Builds a UserDetailsImpl object from a User object.
     *
     * @param user the User object
     * @return a UserDetailsImpl object
     */
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Returns the user's ID.
     *
     * @return the user's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the user's email.
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the user's password.
     *
     * @return the user's password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user's username.
     *
     * @return the user's username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the account is non-expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the account is non-locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return true if the credentials are non-expired, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Checks the equality of this UserDetailsImpl object with another object.
     *
     * @param o the other object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}