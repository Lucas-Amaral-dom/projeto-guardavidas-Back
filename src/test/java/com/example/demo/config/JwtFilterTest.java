package com.example.demo.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JwtFilterTest {

    private JwtUtil jwtUtil;
    private JwtFilter jwtFilter;

    @Test
    @DisplayName("JwtFilter deve ser criado com JwtUtil")
    void deveSerCriadoComJwtUtil() {
        // ARRANGE
        jwtUtil = new JwtUtil();
        
        // ACT
        jwtFilter = new JwtFilter(jwtUtil);

        // ASSERT
        assertNotNull(jwtFilter, "JwtFilter não deve ser nulo");
    }

    @Test
    @DisplayName("JwtFilter deve estender OncePerRequestFilter")
    void deveEstenderOncePerRequestFilter() {
        // ARRANGE & ACT
        boolean isSubclass = org.springframework.web.filter.OncePerRequestFilter.class.isAssignableFrom(JwtFilter.class);

        // ASSERT
        assertTrue(isSubclass, "JwtFilter deve estender OncePerRequestFilter");
    }

    @Test
    @DisplayName("JwtFilter deve ter método doFilterInternal")
    void deveTemMetodoDoFilterInternal() throws NoSuchMethodException {
        // ARRANGE & ACT
        java.lang.reflect.Method method = JwtFilter.class.getDeclaredMethod("doFilterInternal",
            jakarta.servlet.http.HttpServletRequest.class,
            jakarta.servlet.http.HttpServletResponse.class,
            jakarta.servlet.FilterChain.class);

        // ASSERT
        assertNotNull(method, "Método doFilterInternal deve existir");
    }
}
