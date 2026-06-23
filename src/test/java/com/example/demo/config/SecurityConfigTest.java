package com.example.demo.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    @DisplayName("Deve criar PasswordEncoder bean")
    void deveCriarPasswordEncoder() {
        // ARRANGE & ACT
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        // ASSERT
        assertNotNull(encoder, "PasswordEncoder não deve ser nulo");
        assertTrue(encoder instanceof BCryptPasswordEncoder, "Deve ser BCryptPasswordEncoder");
    }

    @Test
    @DisplayName("PasswordEncoder deve criptografar senhas")
    void devePasswordEncoderFuncionar() {
        // ARRANGE
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String senha = "test123";

        // ACT
        String senhaCriptografada = encoder.encode(senha);

        // ASSERT
        assertNotNull(senhaCriptografada);
        assertTrue(!senha.equals(senhaCriptografada));
        assertTrue(encoder.matches(senha, senhaCriptografada));
    }
}
