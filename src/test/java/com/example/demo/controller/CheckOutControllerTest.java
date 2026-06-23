package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entity.Posto;
import com.example.demo.repository.PostoRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CheckOutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostoRepository postoRepository;

    private Posto postoSalvo;

    @BeforeEach
    public void setup() {
        Posto posto = new Posto();
        posto.setNome("Posto para CheckOut");
        posto.setDescricao("Posto de teste para check-out");
        
        postoSalvo = postoRepository.save(posto);
    }

    @Test
    @DisplayName("Deve realizar um Check-out com sucesso enviando arquivo e dados")
    void deveFazerCheckout() throws Exception {
        MockMultipartFile fotoMock = new MockMultipartFile(
                "foto", 
                "teste.jpg", 
                MediaType.IMAGE_JPEG_VALUE, 
                "imagem fake".getBytes()
        );

        mockMvc.perform(multipart("/check/out")
                .file(fotoMock)
                .param("postoId", postoSalvo.getId().toString())
                .param("relatorio", "Relatório do check-out")
                .param("prevencoesMatutino", "5")
                .param("prevencoesVespertino", "3")
                .param("incidentes", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posto").value("Posto para CheckOut"))
                .andExpect(jsonPath("$.horario").exists())
                .andExpect(jsonPath("$.relatorio").value(9))
                .andExpect(jsonPath("$.prevencoesMatutino").value(5));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar fazer Check-out em posto inexistente")
    void deveFalharCheckoutPostoInexistente() throws Exception {
        MockMultipartFile fotoMock = new MockMultipartFile(
                "foto", 
                "teste.jpg", 
                MediaType.IMAGE_JPEG_VALUE, 
                "imagem fake".getBytes()
        );

        mockMvc.perform(multipart("/check/out")
                .file(fotoMock)
                .param("postoId", "999")
                .param("relatorio", "Relatório do check-out")
                .param("prevencoesMatutino", "5")
                .param("prevencoesVespertino", "3")
                .param("incidentes", "1"))
                .andExpect(status().isNotFound()); 
    }
}
