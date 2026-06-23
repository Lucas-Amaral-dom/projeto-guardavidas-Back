package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entity.Usuario;
import com.example.demo.enums.NivelAcesso;
import com.example.demo.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initDatabase(UsuarioRepository repository){
        return args -> {
            Usuario usuario = repository.findByEmail("admin@admin.com")
                    .orElseGet(Usuario::new);

            if (usuario.getId() == null || usuario.getCpf() == null) {
                usuario.setNome("Administrador");
                usuario.setCpf("123");
                usuario.setEmail("admin@admin.com");
                usuario.setNivelAcesso(NivelAcesso.ADMIN);
                usuario.setSenha(passwordEncoder.encode("123456789"));

                repository.save(usuario);

                System.out.println("Usuário ADMIN disponível: CPF 123 / Email admin@admin.com / senha 123456789");
            }else{
                System.out.println("Usuário ADMIN já existe no banco!");
            }
        };
    }
    

}
