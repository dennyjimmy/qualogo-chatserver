package com.qualogo.chatserver.security.jwt;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualogo.chatserver.security.jwt.AuthEntryPointJwt;
import jakarta.servlet.ServletOutputStream;
import org.springframework.http.MediaType;

public class AuthEntryPointJwtTest {

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private Logger logger;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCommenceSetsStatusCode() throws IOException, ServletException {
        authEntryPointJwt.commence(request, response, authException);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void testCommenceSetsContentType() throws IOException, ServletException {
        authEntryPointJwt.commence(request, response, authException);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    public void testCommenceLogsErrorMessage() throws IOException, ServletException {
        when(authException.getMessage()).thenReturn("Unauthorized error");
        authEntryPointJwt.commence(request, response, authException);
        verify(logger).error("Unauthorized error: {}", "Unauthorized error");
    }

    @Test
    public void testCommenceWritesResponseBody() throws IOException, ServletException {
        when(authException.getMessage()).thenReturn("Unauthorized error");
        when(request.getServletPath()).thenReturn("/test-path");

        OutputStream outputStream = mock(OutputStream.class);
        when(response.getOutputStream()).thenReturn((ServletOutputStream) outputStream);

        authEntryPointJwt.commence(request, response, authException);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(objectMapper).writeValue(any(OutputStream.class), captor.capture());

        Map<String, Object> body = captor.getValue();
        assert body.get("status").equals(HttpServletResponse.SC_UNAUTHORIZED);
        assert body.get("error").equals("Unauthorized");
        assert body.get("message").equals("Unauthorized error");
        assert body.get("path").equals("/test-path");
    }

    @Test
    public void testCommenceHandlesNullAuthException() throws IOException, ServletException {
        authEntryPointJwt.commence(request, response, null);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}