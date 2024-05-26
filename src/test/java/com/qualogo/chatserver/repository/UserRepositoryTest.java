package com.qualogo.chatserver.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.qualogo.chatserver.models.User;
import com.qualogo.chatserver.repository.UserRepository;

public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findByUsername("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    public void testExistsByUsername() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        Boolean exists = userRepository.existsByUsername("testuser");
        assertTrue(exists);
    }

    @Test
    public void testExistsByEmail() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        Boolean exists = userRepository.existsByEmail("testuser@example.com");
        assertTrue(exists);
    }
}