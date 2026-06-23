package com.example.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.dto.PostoDTO;

public class PostoDTOTest {

    @Test
    @DisplayName("PostoDTO deve ser instanciável")
    void deveSerInstanciavel() {
        PostoDTO dto = new PostoDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("PostoDTO deve permitir definir e obter nome")
    void devePermitirDefinirNome() {
        PostoDTO dto = new PostoDTO();
        dto.setNome("Posto Central");
        assertEquals("Posto Central", dto.getNome());
    }
}
