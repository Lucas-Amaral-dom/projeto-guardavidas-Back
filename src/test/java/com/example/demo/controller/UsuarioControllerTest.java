package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.AuthDTO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.enums.NivelAcesso;
import com.example.demo.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwt;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String token;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        this.objectMapper = new ObjectMapper();
        this.token = jwt.generateToken("admin@admin.com", NivelAcesso.ADMIN.toString());
    }

    @Test
    @DisplayName("Deve listar todos os usuários")
    void listarUsuarios() throws Exception {
        mockMvc.perform(get("/usuarios")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    void buscarUsuarioPorId() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@usuario.com");
        usuario.setSenha("senha123");
        usuario.setNivelAcesso(NivelAcesso.PADRAO);

        usuario = usuarioRepository.save(usuario);

        mockMvc.perform(get("/usuarios/" + usuario.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("teste@usuario.com"));
    }

    @Test
    @DisplayName("Deve deletar (soft delete) um usuário")
    void deletarUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario.delete@test.com");
        usuario.setSenha("senha123");
        usuario.setNivelAcesso(NivelAcesso.PADRAO);

        usuario = usuarioRepository.save(usuario);

        mockMvc.perform(delete("/usuarios/" + usuario.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
@DisplayName("Deve retornar 400 para JSON inválido no login")
void loginComJsonInvalido() throws Exception {
    String jsonMalFormatado = "{email: \"teste\", senha: }";
    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMalFormatado))
            .andExpect(status().isBadRequest());
}

@Test
@DisplayName("Deve retornar 400 para DTO sem campos obrigatórios (se houver @NotNull)")
void loginComEmailVazio() throws Exception {
    AuthDTO dto = new AuthDTO();
    dto.setEmail(""); // supondo que email tem @NotBlank
    dto.setSenha("123");
    String json = objectMapper.writeValueAsString(dto);
    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
}

}
