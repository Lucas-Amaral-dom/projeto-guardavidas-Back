package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.config.JwtUtil;
import com.example.demo.enums.NivelAcesso;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityTest {

    @Autowired
    private JwtUtil jwt;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void verificaRotaPublica() throws Exception {
        mockMvc.perform(get("/test-security/public"))
        .andExpect(status().isOk())
        .andExpect(content().string("public"));
    }

    @Test
    void verificaRotaAdmin() throws Exception {
        String token = jwt.generateToken(
            "tantofazcomotantofez@admin.com", 
            NivelAcesso.ADMIN.toString()
        );

        mockMvc.perform(get("/test-security/admin")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(content().string("admin"));
    }

    @Test
    void verificaAdminSemLogin() throws Exception {
         mockMvc.perform(get("/test-security/admin"))
        .andExpect(status().isUnauthorized());
    }

    @Test
    void verificaRotaAdminComUsuarioPadrao() throws Exception {
        String token = jwt.generateToken(
            "tantofazcomotantofez@admin.com", 
            NivelAcesso.PADRAO.toString()
        );

        mockMvc.perform(get("/test-security/admin")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
    }

    @Test
@DisplayName("Deve retornar 401 para token inválido (assinatura errada)")
void verificaTokenInvalido() throws Exception {
    String tokenInvalido = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0ZSJ9.invalido";
    mockMvc.perform(get("/test-security/admin")
            .header("Authorization", "Bearer " + tokenInvalido))
            .andExpect(status().isUnauthorized());
}

@Test
@DisplayName("Deve retornar 401 para token mal formatado")
void verificaTokenMalFormatado() throws Exception {
    mockMvc.perform(get("/test-security/admin")
            .header("Authorization", "Bearer xyz"))
            .andExpect(status().isUnauthorized());
}

@Test
@DisplayName("Rota pública com token válido deve ser acessada")
void rotaPublicaComToken() throws Exception {
    String token = jwt.generateToken("qualquer@email.com", NivelAcesso.ADMIN.toString());
    mockMvc.perform(get("/test-security/public")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().string("public"));
}


}
