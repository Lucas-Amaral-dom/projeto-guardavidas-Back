package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CheckinDTO;
import com.example.demo.dto.CheckinResponseDTO;
import com.example.demo.dto.CheckOutDTO;
import com.example.demo.dto.CheckOutResponseDTO;
import com.example.demo.entity.Posto;
import com.example.demo.repository.CheckinRepository;
import com.example.demo.repository.CheckOutRepository;
import com.example.demo.repository.PostoRepository;

@SpringBootTest
@ActiveProfiles("test")
public class CheckServiceTest {

    @Autowired
    private CheckService checkService;

    @Autowired
    private PostoRepository postoRepository;

    @Autowired
    private CheckinRepository checkinRepository;

    @Autowired
    private CheckOutRepository checkOutRepository;

    private Posto posto;

    @BeforeEach
    public void setup() {
        checkinRepository.deleteAll();
        checkOutRepository.deleteAll();
        postoRepository.deleteAll();

        Posto novoPosto = new Posto();
        novoPosto.setNome("Posto Teste");
        novoPosto.setDescricao("Descrição do posto de teste");
        posto = postoRepository.save(novoPosto);
    }

    @Test
    @DisplayName("Deve fazer check-in com sucesso")
    void deveFazerCheckin() throws IOException {
        MultipartFile foto = new MockMultipartFile(
                "foto",
                "teste.jpg",
                "image/jpeg",
                new ByteArrayInputStream("fake image".getBytes())
        );

        CheckinDTO dto = new CheckinDTO();
        dto.setPostoId(posto.getId());
        dto.setFoto(foto);

        CheckinResponseDTO resultado = checkService.checkin(dto);

        assertNotNull(resultado);
        assertEquals("Posto Teste", resultado.getPosto());
        assertNotNull(resultado.getHorario());
    }

    @Test
    @DisplayName("Deve lançar exceção ao fazer check-in com posto inexistente")
    void deveLancarExcecaoCheckinPostoInexistente() throws IOException {
        MultipartFile foto = new MockMultipartFile(
                "foto",
                "teste.jpg",
                "image/jpeg",
                new ByteArrayInputStream("fake image".getBytes())
        );

        CheckinDTO dto = new CheckinDTO();
        dto.setPostoId(999L);
        dto.setFoto(foto);

        assertThrows(IllegalArgumentException.class, () -> checkService.checkin(dto));
    }

    @Test
    @DisplayName("Deve fazer check-out com sucesso")
    void deveFazerCheckout() throws IOException {
        MultipartFile foto = new MockMultipartFile(
                "foto",
                "teste.jpg",
                "image/jpeg",
                new ByteArrayInputStream("fake image".getBytes())
        );

        CheckOutDTO dto = new CheckOutDTO();
        dto.setPostoId(posto.getId());
        dto.setFoto(foto);
        // Novos campos
        dto.setLesoesPorAguaViva(5);
        dto.setIncidentesMatutino(3);
        dto.setIncidentesVespertino(1);

        CheckOutResponseDTO resultado = checkService.checkOut(dto);

        assertNotNull(resultado);
        assertEquals("Posto Teste", resultado.getPosto());
        assertEquals(9, resultado.getRelatorio()); // 5 + 3 + 1
        assertEquals(5, resultado.getLesoesPorAguaViva());
        assertEquals(3, resultado.getIncidentesMatutino());
        assertEquals(1, resultado.getIncidentesVespertino());
    }

    @Test
    @DisplayName("Deve lançar exceção ao fazer check-out com posto inexistente")
    void deveLancarExcecaoCheckoutPostoInexistente() throws IOException {
        MultipartFile foto = new MockMultipartFile(
                "foto",
                "teste.jpg",
                "image/jpeg",
                new ByteArrayInputStream("fake image".getBytes())
        );

        CheckOutDTO dto = new CheckOutDTO();
        dto.setPostoId(999L);
        dto.setFoto(foto);
        // Valores quaisquer, mas o posto não existe
        dto.setLesoesPorAguaViva(5);
        dto.setIncidentesMatutino(3);
        dto.setIncidentesVespertino(1);

        assertThrows(IllegalArgumentException.class, () -> checkService.checkOut(dto));
    }
}