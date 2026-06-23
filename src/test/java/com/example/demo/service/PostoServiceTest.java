package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.dto.PostoDTO;
import com.example.demo.entity.Posto;
import com.example.demo.repository.PostoRepository;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PostoServiceTest {

    @Autowired
    private PostoService postoService;

    @Autowired
    private PostoRepository postoRepository;

    @BeforeEach
    public void setup() {
        postoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um novo Posto")
    void deveCriarPosto() {
        PostoDTO dto = new PostoDTO();
        dto.setNome("Posto Central");
        dto.setDescricao("Descrição do posto central");

        PostoDTO resultado = postoService.create(dto);

        assertNotNull(resultado.getId());
        assertEquals("Posto Central", resultado.getNome());
        assertEquals("Descrição do posto central", resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve buscar Posto por ID")
    void deveBuscarPostoPorId() {
        PostoDTO dto = new PostoDTO();
        dto.setNome("Posto A");
        dto.setDescricao("Descrição A");

        PostoDTO criado = postoService.create(dto);

        PostoDTO resultado = postoService.read(criado.getId());

        assertEquals("Posto A", resultado.getNome());
        assertEquals("Descrição A", resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve atualizar um Posto")
    void deveAtualizarPosto() {
        PostoDTO dto = new PostoDTO();
        dto.setNome("Posto Original");
        dto.setDescricao("Descrição original");

        PostoDTO criado = postoService.create(dto);

        PostoDTO atualizado = new PostoDTO();
        atualizado.setNome("Posto Atualizado");
        atualizado.setDescricao("Descrição atualizada");

        PostoDTO resultado = postoService.update(criado.getId(), atualizado);

        assertEquals("Posto Atualizado", resultado.getNome());
        assertEquals("Descrição atualizada", resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve fazer soft delete de um Posto")
    void deveFazerSoftDeletePosto() {
        PostoDTO dto = new PostoDTO();
        dto.setNome("Posto para deletar");
        dto.setDescricao("Será deletado");

        PostoDTO criado = postoService.create(dto);

        postoService.softDelete(criado.getId());

        Posto postoDeleted = postoRepository.findById(criado.getId()).orElseThrow();
        assertFalse(postoDeleted.isAtivo());
    }

    @Test
    @DisplayName("Deve listar todos os Postos")
    void deveListarTodosPosots() {
        PostoDTO dto1 = new PostoDTO();
        dto1.setNome("Posto 1");
        dto1.setDescricao("Desc 1");

        PostoDTO dto2 = new PostoDTO();
        dto2.setNome("Posto 2");
        dto2.setDescricao("Desc 2");

        postoService.create(dto1);
        postoService.create(dto2);

        var resultado = postoService.read();

        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar Posto inexistente")
    void deveLancarExcecaoAoBuscarPostoInexistente() {
        assertThrows(Exception.class, () -> postoService.read(999L));
    }
}
