package com.example.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.dto.UsuarioDTO;

public class UsuarioDTOTest {

    @Test
    @DisplayName("UsuarioDTO deve ser instanciável")
    void deveSerInstanciavel() {
        UsuarioDTO dto = new UsuarioDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("UsuarioDTO deve permitir definir e obter email")
    void devePermitirDefinirEmail() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("teste@email.com");
        assertEquals("teste@email.com", dto.getEmail());
    }
}
