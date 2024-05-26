package com.qualogo.chatserver.payload.request;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testGetUsername() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        assertEquals("testUser", loginRequest.getUsername());
    }

    @Test
    public void testSetUsername() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        assertEquals("testUser", loginRequest.getUsername());
    }

    @Test
    public void testGetPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("testPass");
        assertEquals("testPass", loginRequest.getPassword());
    }

    @Test
    public void testSetPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("testPass");
        assertEquals("testPass", loginRequest.getPassword());
    }

    @Test
    public void testNotBlankValidation() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("");
        loginRequest.setPassword("");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertEquals(2, violations.size());
    }
}