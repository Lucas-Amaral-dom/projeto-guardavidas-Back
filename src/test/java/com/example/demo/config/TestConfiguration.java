package com.example.demo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuração compartilhada para testes de integração.
 * Fornece beans comuns durante os testes.
 */
@TestConfiguration
class TestConfig {

    @Bean
    public ObjectMapper testObjectMapper() {
        return new ObjectMapper();
    }
}
