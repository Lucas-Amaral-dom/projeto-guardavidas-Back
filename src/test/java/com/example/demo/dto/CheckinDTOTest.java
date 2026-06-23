package com.example.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.dto.CheckinDTO;

public class CheckinDTOTest {

    @Test
    @DisplayName("CheckinDTO deve ser instanciável")
    void deveSerInstanciavel() {
        CheckinDTO dto = new CheckinDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("CheckinDTO deve permitir definir e obter postoId")
    void devePermitirDefinirPostoId() {
        CheckinDTO dto = new CheckinDTO();
        dto.setPostoId(1L);
        assertEquals(1L, dto.getPostoId());
    }
}
