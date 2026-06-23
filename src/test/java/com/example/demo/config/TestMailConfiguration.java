package com.example.demo.config;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@Configuration
@Profile("test")
public class TestMailConfiguration {

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        return new JavaMailSender() {
            @Override
            public MimeMessage createMimeMessage() {
                return new MimeMessage(Session.getInstance(new Properties()));
            }

            @Override
            public MimeMessage createMimeMessage(InputStream contentStream) {
                try {
                    return new MimeMessage(Session.getInstance(new Properties()), contentStream);
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao criar mensagem de teste", e);
                }
            }

            @Override
            public void send(MimeMessage mimeMessage) {
            }

            @Override
            public void send(MimeMessage... mimeMessages) {
            }

            @Override
            public void send(MimeMessagePreparator mimeMessagePreparator) {
                try {
                    mimeMessagePreparator.prepare(createMimeMessage());
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao preparar mensagem de teste", e);
                }
            }

            @Override
            public void send(MimeMessagePreparator... mimeMessagePreparators) {
                for (MimeMessagePreparator preparator : mimeMessagePreparators) {
                    send(preparator);
                }
            }

            @Override
            public void send(SimpleMailMessage simpleMessage) {
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) {
            }
        };
    }
}
