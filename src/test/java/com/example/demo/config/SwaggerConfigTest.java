package com.example.demo.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

public class SwaggerConfigTest {

    private SwaggerConfig swaggerConfig = new SwaggerConfig();

    @Test
    @DisplayName("Deve criar OpenAPI bean")
    void deveCriarOpenAPI() {
        // ARRANGE & ACT
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        // ASSERT
        assertNotNull(openAPI, "OpenAPI não deve ser nulo");
    }

    @Test
    @DisplayName("OpenAPI deve ter info configurada")
    void deveOpenAPITemInfo() {
        // ARRANGE & ACT
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        // ASSERT
        assertNotNull(info, "Info não deve ser nula");
        assertNotNull(info.getTitle(), "Título deve estar configurado");
        assertTrue(info.getTitle().contains("Guarda Vidas"), "Título deve conter 'Guarda Vidas'");
    }

    @Test
    @DisplayName("OpenAPI deve ter segurança configurada")
    void deveOpenAPITemSeguranca() {
        // ARRANGE & ACT
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        // ASSERT
        assertNotNull(openAPI.getSecurity(), "Security requirements deve estar configurado");
        assertTrue(openAPI.getSecurity().size() > 0, "Deve ter pelo menos um requisito de segurança");
    }

    @Test
    @DisplayName("OpenAPI deve ter Bearer Authentication configurado")
    void deveOpenAPITemBearerAuthentication() {
        // ARRANGE & ACT
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        // ASSERT
        assertNotNull(openAPI.getComponents(), "Components deve estar configurado");
        assertNotNull(openAPI.getComponents().getSecuritySchemes(), "Security schemes deve estar configurado");
        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("Bearer Authentication"),
                "Deve ter Bearer Authentication configurado");
    }
}
