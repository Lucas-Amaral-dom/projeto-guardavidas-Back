package com.example.demo.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setup() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Deve tratar IllegalArgumentException com status 404")
    void deveHandlerIllegalArgumentException() {
        // ARRANGE
        String mensagem = "Recurso não encontrado";
        IllegalArgumentException exception = new IllegalArgumentException(mensagem);

        // ACT
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleIllegalArgumentException(exception);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(mensagem, response.getBody().get("error"));
    }

    @Test
    @DisplayName("Deve tratar HttpMessageNotReadableException com status 400")
    void deveHandlerHttpMessageNotReadable() {
        // ARRANGE
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("JSON parsing error", (Throwable) null);

        // ACT
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleHttpMessageNotReadable(exception);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Requisição inválida", response.getBody().get("error"));
    }

    @Test
    @DisplayName("Deve tratar exceção genérica com status 500")
    void deveHandlerGenericException() {
        // ARRANGE
        String mensagem = "Erro interno do servidor";
        Exception exception = new Exception(mensagem);

        // ACT
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(exception);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(mensagem, response.getBody().get("error"));
    }

    @Test
    @DisplayName("Deve retornar corpo não nulo em caso de exceção genérica")
    void deveRetornarCorpoNaoNuloGenericException() {
        // ARRANGE
        Exception exception = new Exception("Erro teste");

        // ACT
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(exception);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() != null);
        assertTrue(response.getBody().get("error").contains("Erro teste"));
    }

    @Test
    @DisplayName("Deve retornar corpo não nulo em caso de IllegalArgumentException")
    void deveRetornarCorpoNaoNuloIllegalArgumentException() {
        // ARRANGE
        Exception exception = new IllegalArgumentException("Argumento inválido");

        // ACT
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleIllegalArgumentException((IllegalArgumentException) exception);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() != null);
        assertTrue(response.getBody().get("error").contains("Argumento inválido"));
    }
}
