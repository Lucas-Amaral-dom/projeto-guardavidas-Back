package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.entity.Arquivo;
import com.example.demo.repository.ArquivoRepository;

@SpringBootTest
@ActiveProfiles("test")
public class ArquivoServiceTest {

    @Autowired
    private ArquivoService arquivoService;

    @Autowired
    private ArquivoRepository arquivoRepository;

    @BeforeEach
    public void setup() {
        arquivoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve fazer upload de um arquivo com sucesso")
    void deveRealizarUploadArquivo() throws Exception {
        var arquivo = new MockMultipartFile(
                "arquivo",
                "teste.txt",
                "text/plain",
                new ByteArrayInputStream("conteúdo do arquivo".getBytes())
        );

        Arquivo resultado = arquivoService.upload(arquivo);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("teste.txt", resultado.getNome());
        assertEquals("text/plain", resultado.getTipo());
        assertEquals(20, resultado.getTamanho());
        assertNotNull(resultado.getCaminho());
    }

    @Test
    @DisplayName("Deve fazer upload de múltiplos arquivos")
    void deveRealizarUploadMultiplosArquivos() throws Exception {
        var arquivo1 = new MockMultipartFile(
                "arquivo1",
                "teste1.txt",
                "text/plain",
                new ByteArrayInputStream("conteúdo 1".getBytes())
        );

        var arquivo2 = new MockMultipartFile(
                "arquivo2",
                "teste2.txt",
                "text/plain",
                new ByteArrayInputStream("conteúdo 2".getBytes())
        );

        Arquivo resultado1 = arquivoService.upload(arquivo1);
        Arquivo resultado2 = arquivoService.upload(arquivo2);

        assertNotNull(resultado1.getId());
        assertNotNull(resultado2.getId());
        assertNotEquals(resultado1.getId(), resultado2.getId());
        assertEquals(2, arquivoRepository.count());
    }

    @Test
    @DisplayName("Deve fazer upload de arquivo com nome com acentuação")
    void deveRealizarUploadArquivoComAcentuacao() throws Exception {
        var arquivo = new MockMultipartFile(
                "arquivo",
                "testeção.txt",
                "text/plain",
                new ByteArrayInputStream("conteúdo".getBytes())
        );

        Arquivo resultado = arquivoService.upload(arquivo);

        assertNotNull(resultado);
        assertEquals("testeção.txt", resultado.getNome());
    }

    @Test
    @DisplayName("Deve fazer upload de arquivo tipo imagem")
    void deveRealizarUploadArquivoImagem() throws Exception {
        byte[] imagemBytes = new byte[]{-119, 80, 78, 71}; // PNG magic number
        
        var arquivo = new MockMultipartFile(
                "arquivo",
                "imagem.png",
                "image/png",
                new ByteArrayInputStream(imagemBytes)
        );

        Arquivo resultado = arquivoService.upload(arquivo);

        assertNotNull(resultado);
        assertEquals("imagem.png", resultado.getNome());
        assertEquals("image/png", resultado.getTipo());
    }
}
