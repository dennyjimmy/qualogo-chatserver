package com.qualogo.chatserver.security.services;

import com.qualogo.chatserver.models.ERole;
import com.qualogo.chatserver.models.Role;
import com.qualogo.chatserver.models.User;
import com.qualogo.chatserver.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDetailsImplTest {

    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
        role = mock(Role.class);
        when(role.getName()).thenReturn(ERole.ROLE_USER);

        user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getUsername()).thenReturn("testuser");
        when(user.getEmail()).thenReturn("testuser@example.com");
        when(user.getPassword()).thenReturn("password");
        when(user.getRoles()).thenReturn(Collections.singleton(role));
    }

    @Test
    public void testBuild() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        assertEquals(user.getId(), userDetails.getId());
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getEmail(), userDetails.getEmail());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testGetAuthorities() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testGetId() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        assertEquals(1L, userDetails.getId());
    }

    @Test
    public void testGetEmail() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        assertEquals("testuser@example.com", userDetails.getEmail());
    }

    @Test
    public void testGetPassword() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    public void testGetUsername() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    public void testIsAccountNonExpired() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void testEqualsSameObject() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        assertTrue(userDetails.equals(userDetails));
    }

    @Test
    public void testEqualsDifferentObjectSameValues() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.build(user);
        UserDetailsImpl userDetails2 = UserDetailsImpl.build(user);
        assertTrue(userDetails1.equals(userDetails2));
    }

    @Test
    public void testEqualsNull() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        assertFalse(userDetails.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        assertFalse(userDetails.equals(new Object()));
    }

    @Test
    public void testEqualsDifferentId() {
        User user2 = mock(User.class);
        when(user2.getId()).thenReturn(2L);
        when(user2.getUsername()).thenReturn("testuser2");
        when(user2.getEmail()).thenReturn("testuser2@example.com");
        when(user2.getPassword()).thenReturn("password2");
        when(user2.getRoles()).thenReturn(Collections.singleton(role));

        UserDetailsImpl userDetails1 = UserDetailsImpl.build(user);
        UserDetailsImpl userDetails2 = UserDetailsImpl.build(user2);

        assertFalse(userDetails1.equals(userDetails2));
    }

    @Test
    public void testBuildWithEmptyRoles() {
        when(user.getRoles()).thenReturn(Collections.emptySet());
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    public void testBuildWithMultipleRoles() {
        Role roleAdmin = mock(Role.class);
        when(roleAdmin.getName()).thenReturn(ERole.ROLE_ADMIN);

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        roles.add(roleAdmin);

        when(user.getRoles()).thenReturn(roles);

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        assertNotNull(userDetails.getAuthorities());
        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}