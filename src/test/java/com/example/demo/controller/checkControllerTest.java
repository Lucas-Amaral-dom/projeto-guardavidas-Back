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
public class checkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostoRepository postoRepository;

    private Posto postoSalvo;

    @BeforeEach
    public void setup() {
        Posto posto = new Posto();
        posto.setNome("Posto Central");
        posto.setDescricao("Posto de teste para check-in");
        
        postoSalvo = postoRepository.save(posto);
    }

    @Test
    @DisplayName("Deve realizar um Check-in com sucesso enviando arquivo")
    void deveFazerCheckin() throws Exception {
        MockMultipartFile fotoMock = new MockMultipartFile(
                "foto", 
                "teste.jpg", 
                MediaType.IMAGE_JPEG_VALUE, 
                "imagem fake".getBytes()
        );

        mockMvc.perform(multipart("/check/in")
                .file(fotoMock)
                .param("postoId", postoSalvo.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posto").value("Posto Central"))
                .andExpect(jsonPath("$.horario").exists());
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar fazer Check-in em posto inexistente")
    void deveFalharCheckinPostoInexistente() throws Exception {
        MockMultipartFile fotoMock = new MockMultipartFile(
                "foto", 
                "teste.jpg", 
                MediaType.IMAGE_JPEG_VALUE, 
                "imagem fake".getBytes()
        );

        mockMvc.perform(multipart("/check/in")
                .file(fotoMock)
                .param("postoId", "999"))
                .andExpect(status().isNotFound()); 
    }
}
