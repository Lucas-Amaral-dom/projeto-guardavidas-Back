package com.example.demo.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtFilterTestExtended {

    @Test
    @DisplayName("JwtFilter deve ser instanciável com JwtUtil")
    void deveSerInstanciavel() {
        JwtUtil jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "chave-secreta-de-teste-com-pelo-menos-32-caracteres-aqui!");
        jwtUtil.init();

        JwtFilter filter = new JwtFilter(jwtUtil);
        assertNotNull(filter);
    }

    @Test
    @DisplayName("JwtFilter deve ser subclasse de OncePerRequestFilter")
    void deveEstenderOncePerRequestFilter() {
        boolean isSubclass = org.springframework.web.filter.OncePerRequestFilter.class
                .isAssignableFrom(JwtFilter.class);
        assertTrue(isSubclass);
    }
}
