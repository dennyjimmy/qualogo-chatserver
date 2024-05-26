package com.qualogo.chatserver.controller;

import com.qualogo.chatserver.controllers.AuthController;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.qualogo.chatserver.models.ERole;
import com.qualogo.chatserver.models.Role;
import com.qualogo.chatserver.models.User;
import com.qualogo.chatserver.payload.request.LoginRequest;
import com.qualogo.chatserver.payload.request.SignupRequest;
import com.qualogo.chatserver.payload.response.JwtResponse;
import com.qualogo.chatserver.payload.response.MessageResponse;
import com.qualogo.chatserver.repository.RoleRepository;
import com.qualogo.chatserver.repository.UserRepository;
import com.qualogo.chatserver.security.jwt.JwtUtils;
import com.qualogo.chatserver.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder encoder;

    @Mock
    JwtUtils jwtUtils;

    @InjectMocks
    AuthController authController;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testUser", "testEmail", "testPassword", List.of());
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String jwt = "testJwt";
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwt);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals(jwt, jwtResponse.getAccessToken());
        assertEquals("testUser", jwtResponse.getUsername());
    }

    @Test
    public void testRegisterUserUsernameTaken() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("testUser");
        signUpRequest.setEmail("testEmail");
        signUpRequest.setPassword("testPassword");

        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Error: Username is already taken!", messageResponse.getMessage());
    }

    @Test
    public void testRegisterUserEmailTaken() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("testUser");
        signUpRequest.setEmail("testEmail");
        signUpRequest.setPassword("testPassword");

        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("testEmail")).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Error: Email is already in use!", messageResponse.getMessage());
    }

    @Test
    public void testRegisterUserSuccess() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("testUser");
        signUpRequest.setEmail("testEmail");
        signUpRequest.setPassword("testPassword");
        signUpRequest.setRole(Set.of("user"));

        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("testEmail")).thenReturn(false);

        Role userRole = new Role(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(encoder.encode("testPassword")).thenReturn("encodedPassword");

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUserWithRoles() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("testUser");
        signUpRequest.setEmail("testEmail");
        signUpRequest.setPassword("testPassword");
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("mod");
        signUpRequest.setRole(roles);

        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("testEmail")).thenReturn(false);

        Role adminRole = new Role(ERole.ROLE_ADMIN);
        Role modRole = new Role(ERole.ROLE_MODERATOR);

        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        when(roleRepository.findByName(ERole.ROLE_MODERATOR)).thenReturn(Optional.of(modRole));
        when(encoder.encode("testPassword")).thenReturn("encodedPassword");

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());

        verify(userRepository, times(1)).save(any(User.class));
    }
}
