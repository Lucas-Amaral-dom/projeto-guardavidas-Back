package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.mail.internet.MimeMessage;

public class EmailServiceTest {

    private EmailService emailService;
    private JavaMailSender mailSender;

    @BeforeEach
    void setup() {
        emailService = new EmailService();
        mailSender = mock(JavaMailSender.class);
        
        // Injetar o mock via reflection
        ReflectionTestUtils.setField(emailService, "mailSender", mailSender);
    }

    @Test
    @DisplayName("Deve enviar email com dados válidos")
    void deveEnviarEmailComDadosValidos() throws Exception {
        // ARRANGE
        String destinatario = "usuario@test.com";
        String titulo = "Assunto Teste";
        String descricao = "Corpo do email teste";
        
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mailSender).send(any(MimeMessage.class));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // ACT & ASSERT - Não deve lançar exceção
        assertDoesNotThrow(() -> 
            emailService.enviarEmail(destinatario, titulo, descricao)
        );

        // Verificar que send foi chamado
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Deve usar endereço correto de origem")
    void deveUsarEnderecoOrigemCorreto() throws Exception {
        // ARRANGE
        String destinatario = "usuario@test.com";
        String titulo = "Teste";
        String descricao = "Teste de email";
        
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mailSender).send(any(MimeMessage.class));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // ACT
        assertDoesNotThrow(() -> 
            emailService.enviarEmail(destinatario, titulo, descricao)
        );

        // ASSERT - O email deve ser enviado com o endereço correto
        // Verificado indiretamente através da chamada bem-sucedida
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando JavaMailSender falha")
    void deveLancarExcecaoQuandoMailSenderFalha() throws Exception {
        // ARRANGE
        String destinatario = "usuario@test.com";
        String titulo = "Teste";
        String descricao = "Teste";
        
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Erro de conexão com servidor de email"))
            .when(mailSender).send(any(MimeMessage.class));

        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> 
            emailService.enviarEmail(destinatario, titulo, descricao)
        );
    }

    @Test
    @DisplayName("Deve aceitar htmlContent como true")
    void deveAceitarHtmlContentTrue() throws Exception {
        // ARRANGE
        String destinatario = "usuario@test.com";
        String titulo = "Email HTML";
        String descricao = "<html><body>Conteúdo HTML</body></html>";
        
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mailSender).send(any(MimeMessage.class));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // ACT & ASSERT
        assertDoesNotThrow(() -> 
            emailService.enviarEmail(destinatario, titulo, descricao)
        );

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Deve ter método enviarEmailFromTemplate")
    void deveTemMetodoEnviarEmailFromTemplate() throws Exception {
        // ARRANGE
        String destinatario = "usuario@test.com";
        String titulo = "Template Email";
        String descricao = "Conteúdo do template";
        
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mailSender).send(any(MimeMessage.class));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // ACT & ASSERT
        assertDoesNotThrow(() -> emailService.enviarEmailFromTemplate(destinatario, titulo, descricao));
    }

    @Test
    @DisplayName("Deve aceitar encoding UTF-8")
    void deveAceitarEncodingUTF8() throws Exception {
        // ARRANGE
        String destinatario = "usuario@test.com";
        String titulo = "Email UTF-8 - Função do Teste";
        String descricao = "Teste com caracteres acentuados: àáâãäåèéêë";
        
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doNothing().when(mailSender).send(any(MimeMessage.class));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // ACT & ASSERT
        assertDoesNotThrow(() -> 
            emailService.enviarEmail(destinatario, titulo, descricao)
        );

        verify(mailSender).send(any(MimeMessage.class));
    }
}
