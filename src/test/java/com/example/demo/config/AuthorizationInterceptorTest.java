package com.example.demo.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationInterceptorTest {

    private AuthorizationInterceptor interceptor;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        interceptor = new AuthorizationInterceptor();
        jwtUtil = mock(JwtUtil.class);
        ReflectionTestUtils.setField(interceptor, "jwtUtil", jwtUtil);
    }

    @Test
    @DisplayName("AuthorizationInterceptor deve ser HandlerInterceptor")
    void deveSerHandlerInterceptor() {
        // ARRANGE & ACT & ASSERT
        assertTrue(interceptor instanceof HandlerInterceptor);
    }

    @Test
    @DisplayName("Deve permitir acesso a swagger-ui")
    void devePeermitirSwaggerUI() throws Exception {
        // ARRANGE
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        when(request.getDispatcherType()).thenReturn(jakarta.servlet.DispatcherType.REQUEST);
        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");

        // ACT
        boolean result = interceptor.preHandle(request, response, new Object());

        // ASSERT
        assertTrue(result, "Deve permitir acesso a swagger-ui");
    }

    @Test
    @DisplayName("Deve permitir acesso a v3/api-docs")
    void devePeermitirApiDocs() throws Exception {
        // ARRANGE
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        when(request.getDispatcherType()).thenReturn(jakarta.servlet.DispatcherType.REQUEST);
        when(request.getRequestURI()).thenReturn("/v3/api-docs/swagger-config");

        // ACT
        boolean result = interceptor.preHandle(request, response, new Object());

        // ASSERT
        assertTrue(result, "Deve permitir acesso a v3/api-docs");
    }

    @Test
    @DisplayName("Deve permitir ERROR dispatcher type")
    void devePeermitirErrorDispatcherType() throws Exception {
        // ARRANGE
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        when(request.getDispatcherType()).thenReturn(jakarta.servlet.DispatcherType.ERROR);

        // ACT
        boolean result = interceptor.preHandle(request, response, new Object());

        // ASSERT
        assertTrue(result, "Deve permitir ERROR dispatcher type");
    }
}
