package com.qualogo.chatserver.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.qualogo.chatserver.models.ERole;
import com.qualogo.chatserver.models.Role;
import org.mockito.Mockito;

@ExtendWith(MockitoExtension.class)
public class RoleRepositoryTest {

    public class EmptyResultDataAccessException extends RuntimeException {
        public EmptyResultDataAccessException(int expectedSize) {
            super("Expected size: " + expectedSize);
        }
    }
    
    @Mock
    private RoleRepository roleRepository;

    private Role adminRole;

    @BeforeEach
    public void setUp() {
        adminRole = new Role();
        adminRole.setId(1);
        adminRole.setName(ERole.ROLE_ADMIN);
    }

    @Test
    public void testFindByName_RoleExists() {
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        Optional<Role> foundRole = roleRepository.findByName(ERole.ROLE_ADMIN);

        assertTrue(foundRole.isPresent());
        assertEquals(adminRole, foundRole.get());
    }

    @Test
    public void testFindByName_RoleDoesNotExist() {
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.empty());

        Optional<Role> foundRole = roleRepository.findByName(ERole.ROLE_USER);

        assertFalse(foundRole.isPresent());
    }

    @Test
    public void testFindByName_NullValue() {
        when(roleRepository.findByName(null)).thenReturn(Optional.empty());

        Optional<Role> foundRole = roleRepository.findByName(null);

        assertFalse(foundRole.isPresent());
    }

    @Test
    public void testSaveRole() {
        when(roleRepository.save(any(Role.class))).thenReturn(adminRole);

        Role savedRole = roleRepository.save(adminRole);

        assertEquals(adminRole, savedRole);
    }

@Test
    public void testDeleteRoleById_Success() {
        Mockito.doNothing().when(roleRepository).deleteById(1L);

        roleRepository.deleteById(1L);

        Mockito.verify(roleRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteRoleById_RoleDoesNotExist() {
        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(roleRepository).deleteById(1L);

        try {
            roleRepository.deleteById(1L);
        } catch (EmptyResultDataAccessException e) {
            assertFalse(e.getMessage().isEmpty());
        }

        Mockito.verify(roleRepository, Mockito.times(1)).deleteById(1L);
    }
}