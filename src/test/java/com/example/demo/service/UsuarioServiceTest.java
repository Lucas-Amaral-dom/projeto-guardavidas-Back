package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.dto.RecuperacaoSolicitacaoDTO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.enums.NivelAcesso;
import com.example.demo.repository.UsuarioRepository;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setup() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um novo Usuário")
    void deveCriarUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("novo@usuario.com");
        dto.setSenha("senha123");

        UsuarioDTO resultado = usuarioService.create(dto);

        assertNotNull(resultado.getId());
        assertEquals("novo@usuario.com", resultado.getEmail());
    }

    @Test
    @DisplayName("Deve buscar Usuário por ID")
    void deveBuscarUsuarioPorId() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("usuario@test.com");
        dto.setSenha("senha123");

        UsuarioDTO criado = usuarioService.create(dto);

        UsuarioDTO resultado = usuarioService.read(criado.getId());

        assertEquals("usuario@test.com", resultado.getEmail());
    }

    @Test
    @DisplayName("Deve atualizar um Usuário")
    void deveAtualizarUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("usuario.antigo@test.com");
        dto.setSenha("senha123");

        UsuarioDTO criado = usuarioService.create(dto);

        UsuarioDTO atualizado = new UsuarioDTO();
        atualizado.setEmail("usuario.novo@test.com");
        atualizado.setSenha("novaSenha123");

        UsuarioDTO resultado = usuarioService.update(criado.getId(), atualizado);

        assertEquals("usuario.novo@test.com", resultado.getEmail());
    }

    @Test
    @DisplayName("Deve fazer soft delete de um Usuário")
    void deveFazerSoftDeleteUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("usuario.delete@test.com");
        dto.setSenha("senha123");

        UsuarioDTO criado = usuarioService.create(dto);

        usuarioService.softDelete(criado.getId());

        Usuario usuarioDeleted = usuarioRepository.findById(criado.getId()).orElseThrow();
        assertFalse(usuarioDeleted.isAtivo());
    }

    @Test
    @DisplayName("Deve listar todos os Usuários")
    void deveListarTodosUsuarios() {
        UsuarioDTO dto1 = new UsuarioDTO();
        dto1.setEmail("usuario1@test.com");
        dto1.setSenha("senha123");

        UsuarioDTO dto2 = new UsuarioDTO();
        dto2.setEmail("usuario2@test.com");
        dto2.setSenha("senha123");

        usuarioService.create(dto1);
        usuarioService.create(dto2);

        var resultado = usuarioService.read();

        assertTrue(resultado.size() >= 2);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar Usuário inexistente")
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        assertThrows(Exception.class, () -> usuarioService.read(999L));
    }
    @Test
@DisplayName("Deve solicitar código de recuperação com sucesso")
void deveSolicitarCodigoComSucesso() {
    // Arrange
    Usuario usuario = new Usuario();
    usuario.setEmail("recuperacao@teste.com");
    usuario.setSenha("123");
    usuario = usuarioRepository.save(usuario);

    RecuperacaoSolicitacaoDTO dto = new RecuperacaoSolicitacaoDTO();
    dto.setEmail("recuperacao@teste.com");

    // Act & Assert (não lança exceção)
    assertDoesNotThrow(() -> usuarioService.solicitarCodigo(dto));

    // Verifica se o código foi gerado e a expiração foi setada
    Usuario atualizado = usuarioRepository.findById(usuario.getId()).orElseThrow();
    assertNotNull(atualizado.getCodigoRecuperacao());
    assertNotNull(atualizado.getCodigoRecuperacaoExpiracao());
}

@Test
@DisplayName("Deve lançar exceção ao solicitar código para e-mail inexistente")
void deveLancarExcecaoEmailNaoEncontrado() {
    RecuperacaoSolicitacaoDTO dto = new RecuperacaoSolicitacaoDTO();
    dto.setEmail("naoexiste@teste.com");
    assertThrows(RuntimeException.class, () -> usuarioService.solicitarCodigo(dto));
}
}

