package com.example.demo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestConfiguration
public class TestConfigurationForAuth {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

