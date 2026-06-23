package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.config.JwtFilter;
import com.example.demo.config.JwtUtil;

import jakarta.servlet.FilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock private JwtUtil jwtUtil;
    @Mock private FilterChain chain;
    @InjectMocks private JwtFilter filter;

    @Test
    void deveAutenticarComTokenValido() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token.valido");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.validateToken("token.valido")).thenReturn(true);
        when(jwtUtil.extractUsername("token.valido")).thenReturn("user");
        when(jwtUtil.extractRole("token.valido")).thenReturn("ADMIN");

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
