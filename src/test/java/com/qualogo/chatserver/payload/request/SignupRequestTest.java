package com.qualogo.chatserver.payload.request;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import java.util.HashSet;

public class SignupRequestTest {

    private Validator validator;
    private SignupRequest signupRequest;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        signupRequest = new SignupRequest();
    }

    @Test
    public void testValidSignupRequest() {
        signupRequest.setUsername("validUser");
        signupRequest.setEmail("valid@example.com");
        signupRequest.setPassword("validPassword");
        signupRequest.setRole(new HashSet<>());

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidUsernameTooShort() {
        signupRequest.setUsername("ab");
        signupRequest.setEmail("valid@example.com");
        signupRequest.setPassword("validPassword");
        signupRequest.setRole(new HashSet<>());

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testInvalidEmail() {
        signupRequest.setUsername("validUser");
        signupRequest.setEmail("invalid-email");
        signupRequest.setPassword("validPassword");
        signupRequest.setRole(new HashSet<>());

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testInvalidPasswordTooShort() {
        signupRequest.setUsername("validUser");
        signupRequest.setEmail("valid@example.com");
        signupRequest.setPassword("short");
        signupRequest.setRole(new HashSet<>());

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testNullUsername() {
        signupRequest.setUsername(null);
        signupRequest.setEmail("valid@example.com");
        signupRequest.setPassword("validPassword");
        signupRequest.setRole(new HashSet<>());

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }
}