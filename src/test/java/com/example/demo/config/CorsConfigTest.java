package com.example.demo.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

public class CorsConfigTest {

    private CorsConfig corsConfig = new CorsConfig();

    @Test
    @DisplayName("Deve criar CorsFilter bean")
    void deveCriarCorsFilter() {
        // ARRANGE & ACT
        CorsFilter filter = corsConfig.corsFilter();

        // ASSERT
        assertNotNull(filter, "CorsFilter não deve ser nulo");
        assertTrue(filter instanceof CorsFilter, "Deve ser instância de CorsFilter");
    }

    @Test
    @DisplayName("CorsFilter deve estar configurado")
    void deveCorsFilterEstarConfigurado() {
        // ARRANGE & ACT
        CorsFilter filter = corsConfig.corsFilter();

        // ASSERT
        assertNotNull(filter, "CorsFilter deve estar configurado");
    }
}
