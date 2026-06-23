package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.dto.AuthDTO;
import com.example.demo.dto.RecuperacaoSolicitacaoDTO;
import com.example.demo.dto.RecuperarSenhaDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.enums.NivelAcesso;
import com.example.demo.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.context.annotation.Import;
import com.example.demo.config.TestConfigurationForAuth;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@Import(TestConfigurationForAuth.class)
public class authControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        // Limpa o banco para garantir isolamento e cria um usuário de teste
        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setEmail("teste@auth.com");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setNivelAcesso(NivelAcesso.ADMIN);

        usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar um token")
    void deveLogarComSucesso() throws Exception {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail("teste@auth.com");
        authDTO.setSenha("senha123");

        String json = objectMapper.writeValueAsString(authDTO);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.tipo").value("ADMIN"));
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized com credenciais inválidas")
    void deveFalharLoginComSenhaIncorreta() throws Exception {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail("teste@auth.com");
        authDTO.setSenha("senhaErrada"); // Senha errada

        String json = objectMapper.writeValueAsString(authDTO);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }
    @Test
@DisplayName("Deve solicitar código de recuperação (endpoint público)")
void deveSolicitarCodigo() throws Exception {
    // Primeiro cria um usuário no banco
    Usuario usuario = new Usuario();
    usuario.setEmail("recup@teste.com");
    usuario.setSenha(passwordEncoder.encode("123"));
    usuarioRepository.save(usuario);

    RecuperacaoSolicitacaoDTO dto = new RecuperacaoSolicitacaoDTO();
    dto.setEmail("recup@teste.com");

    mockMvc.perform(post("/auth/recuperar-senha/solicitar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("email enviado com sucesso"));
}

@Test
@DisplayName("Deve retornar erro ao solicitar código para e-mail inexistente")
void deveErroAoSolicitarCodigoEmailInexistente() throws Exception {
    RecuperacaoSolicitacaoDTO dto = new RecuperacaoSolicitacaoDTO();
    dto.setEmail("naoexiste@teste.com");

    mockMvc.perform(post("/auth/recuperar-senha/solicitar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Usuário não encontrado"));
}

@Test
@DisplayName("Deve alterar senha com código válido")
void deveAlterarSenhaComSucesso() throws Exception {
    // Cria usuário com código de recuperação
    Usuario usuario = new Usuario();
    usuario.setEmail("alterar@teste.com");
    usuario.setSenha(passwordEncoder.encode("old"));
    usuario.setCodigoRecuperacao("12345678");
    usuario.setCodigoRecuperacaoExpiracao(LocalDateTime.now().plusMinutes(10));
    usuarioRepository.save(usuario);

    RecuperarSenhaDTO dto = new RecuperarSenhaDTO();
    dto.setEmail("alterar@teste.com");
    dto.setCodigo("12345678");
    dto.setNovaSenha("newPassword");

    mockMvc.perform(post("/auth/recuperar-senha/alterar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("senha alterada com sucesso"));
}
}
