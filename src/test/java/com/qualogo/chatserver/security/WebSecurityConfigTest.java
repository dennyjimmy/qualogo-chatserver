package com.qualogo.chatserver.security;

import com.qualogo.chatserver.security.jwt.AuthEntryPointJwt;
import com.qualogo.chatserver.security.jwt.AuthTokenFilter;
import com.qualogo.chatserver.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WebSecurityConfigTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private AuthEntryPointJwt unauthorizedHandler;

    @Mock
    private AuthenticationConfiguration authConfig;

    @InjectMocks
    private WebSecurityConfig webSecurityConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticationJwtTokenFilter() {
        AuthTokenFilter authTokenFilter = webSecurityConfig.authenticationJwtTokenFilter();
        assertNotNull(authTokenFilter);
    }

    @Test
    public void testAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = webSecurityConfig.authenticationProvider();
        assertNotNull(authProvider);
    }

    @Test
    public void testAuthenticationManager() throws Exception {
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        when(authConfig.getAuthenticationManager()).thenReturn(mockAuthManager);

        AuthenticationManager authManager = webSecurityConfig.authenticationManager(authConfig);
        assertNotNull(authManager);
        assertEquals(mockAuthManager, authManager);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = webSecurityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof PasswordEncoder);
    }

    @Test
    public void testFilterChain() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class);
        when(http.csrf(any())).thenReturn(http);
        when(http.exceptionHandling(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.authenticationProvider(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);

        SecurityFilterChain filterChain = webSecurityConfig.filterChain(http);
        //assertNotNull(filterChain);

        verify(http).csrf(any());
        verify(http).exceptionHandling(any());
        verify(http).sessionManagement(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).authenticationProvider(any());
        verify(http).addFilterBefore(any(), any());
    }
}