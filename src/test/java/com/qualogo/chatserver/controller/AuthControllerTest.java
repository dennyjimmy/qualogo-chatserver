package com.qualogo.chatserver.controller;

import com.qualogo.chatserver.controllers.AuthController;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Mock
    Authentication authentication;

    @BeforeEach
    public void setUp() {
        // Setup common mock behavior here if needed
    }

    @Test
    public void testAuthenticateUser_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getUsername()).thenReturn("user");
        when(userDetails.getEmail()).thenReturn("user@example.com");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof JwtResponse);
        assertEquals("jwtToken", ((JwtResponse) response.getBody()).getAccessToken());
    }

    @Test
    public void testAuthenticateUser_InvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        assertThrows(RuntimeException.class, () -> {
            authController.authenticateUser(loginRequest);
        });
    }

    @Test
    public void testRegisterUser_Success() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");
        signupRequest.setRole(Collections.singleton("user"));

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(encoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameAlreadyExists() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("existingUser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Username is already taken!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("existing@example.com");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Email is already in use!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_RoleNotFound() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");
        signupRequest.setRole(Collections.singleton("admin"));

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.registerUser(signupRequest);
        });

        assertEquals("Error: Role is not found.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_DefaultRoleAssigned() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(encoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_NullRole() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");
        signupRequest.setRole(null);

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(encoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_EmptyRole() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");
        signupRequest.setRole(Collections.emptySet());

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(encoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_InvalidRole() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");
        signupRequest.setRole(Collections.singleton("invalidRole"));

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(encoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.registerUser(signupRequest);
        });

        assertEquals("Error: Role is not found.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_NullUsername() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(null);
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");
        signupRequest.setRole(Collections.singleton("user"));

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Username is already taken!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_NullEmail() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail(null);
        signupRequest.setPassword("password");
        signupRequest.setRole(Collections.singleton("user"));

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Email is already in use!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_NullPassword() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword(null);
        signupRequest.setRole(Collections.singleton("user"));

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Password cannot be null!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_EmptyUsername() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");
        signupRequest.setRole(Collections.singleton("user"));

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Username is already taken!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_EmptyEmail() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("");
        signupRequest.setPassword("password");
        signupRequest.setRole(Collections.singleton("user"));

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Email is already in use!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_EmptyPassword() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newUser");
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("");
        signupRequest.setRole(Collections.singleton("user"));

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof MessageResponse);
        assertEquals("Error: Password cannot be empty!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    
}
