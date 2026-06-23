package com.example.demo.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WebMvcConfigTest {

    private WebMvcConfig webMvcConfig = new WebMvcConfig();

    @Test
    @DisplayName("WebMvcConfig deve existir")
    void deveWebMvcConfigExistir() {
        // ARRANGE & ACT & ASSERT
        assertNotNull(webMvcConfig, "WebMvcConfig não deve ser nulo");
    }

    @Test
    @DisplayName("Deve ter método addInterceptors")
    void deveTemMetodoAddInterceptors() throws NoSuchMethodException {
        // ARRANGE & ACT
        java.lang.reflect.Method method = WebMvcConfig.class.getMethod("addInterceptors", InterceptorRegistry.class);

        // ASSERT
        assertNotNull(method, "Método addInterceptors deve existir");
    }
}
