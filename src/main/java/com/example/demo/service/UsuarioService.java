package com.example.demo.service;

import java.time.LocalDateTime;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.dto.RecuperacaoSolicitacaoDTO;
import com.example.demo.dto.RecuperarSenhaDTO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService extends BaseService<Usuario, UsuarioDTO> {

    private final UsuarioRepository repository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UsuarioService(UsuarioRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    @Transactional
    public UsuarioDTO create(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setCpf(somenteDigitos(dto.getCpf()));
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        return toDto(repository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioDTO update(Long id, UsuarioDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setNome(dto.getNome());
        usuario.setCpf(somenteDigitos(dto.getCpf()));
        usuario.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        return toDto(repository.save(usuario));
    }
    
    @Transactional
    public void solicitarCodigo(RecuperacaoSolicitacaoDTO dto) {
        String email = dto.getEmail();
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        SecureRandom secureRandom = new SecureRandom();
        String codigo = String.valueOf(10000000 + secureRandom.nextInt(90000000));

        usuario.setCodigoRecuperacao(codigo);
        usuario.setCodigoRecuperacaoExpiracao(LocalDateTime.now().plusMinutes(20));
        repository.save(usuario);

        try {
            emailService.enviarEmail(email, "Recuperação de senha", "Seu código é: " + codigo);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao enviar e-mail");
        }
    }

    @Transactional
    public void alterarSenha(RecuperarSenhaDTO dto) {
        Usuario usuario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (usuario.getCodigoRecuperacao() == null || 
            !usuario.getCodigoRecuperacao().equals(dto.getCodigo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhuma solicitaçao de recuperacao feita");
        }

        if (usuario.getCodigoRecuperacaoExpiracao() == null ||
            usuario.getCodigoRecuperacaoExpiracao().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhuma solicitação de recuperação feita");
        }
        if (!usuario.getCodigoRecuperacao().equals(dto.getCodigo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código inválido");
        }
        if (usuario.getCodigoRecuperacaoExpiracao().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código expirado");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(dto.getNovaSenha());
        usuario.setSenha(novaSenhaCriptografada);
        usuario.setCodigoRecuperacao(null);
        usuario.setCodigoRecuperacaoExpiracao(null);
        repository.save(usuario);
    }

    private String somenteDigitos(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.replaceAll("\\D", "");
    }
    
}
