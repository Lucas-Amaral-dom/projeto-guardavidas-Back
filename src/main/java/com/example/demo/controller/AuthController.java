package com.example.demo.controller;

import java.util.Map;
import java.util.Optional;
import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.annotations.Public;
import com.example.demo.config.JwtUtil;
import com.example.demo.dto.AuthDTO;
import com.example.demo.dto.RecuperacaoSolicitacaoDTO;
import com.example.demo.dto.RecuperarSenhaDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/auth", "/api/auth"})
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
@Public
public ResponseEntity<?> login(@RequestBody @Valid AuthDTO dto) {
    String cpfField = dto.getCpf();   // pode ser CPF ou email
    String email = dto.getEmail();
    String senha = dto.getSenha();

    String cpf = null;
    // Se o campo "cpf" contém @, é um email → move para a variável email
    if (cpfField != null && cpfField.contains("@")) {
        email = cpfField;
    } else {
        cpf = somenteDigitos(cpfField);
    }

    Optional<Usuario> usuarioOpt = Optional.empty();
    if (cpf != null && !cpf.isBlank()) {
        usuarioOpt = cpf.length() >= 11
                ? usuarioRepository.findByCpf(cpf)
                : usuarioRepository.findFirstByCpfStartingWithAndAtivoTrueOrderByIdAsc(cpf);
    } else if (email != null && !email.isBlank()) {
        usuarioOpt = usuarioRepository.findByEmail(email);
    } else {
        return ResponseEntity.badRequest().body(Map.of("error", "CPF ou email deve ser preenchido."));
    }

    if (usuarioOpt.isPresent() && passwordEncoder.matches(senha, usuarioOpt.get().getSenha())) {
        Usuario usuario = usuarioOpt.get();
        String nivelAcesso = usuario.getNivelAcesso().toString();
        String tipo = usuario.getNivelAcesso().name().equals("ADMIN") ? "ADMIN" : "GUARDA_VIDAS";

        String token = jwtUtil.generateToken(
                usuario.getCpf() != null ? usuario.getCpf() : usuario.getEmail(),
                nivelAcesso);

        return ResponseEntity.ok(loginResponse(usuario, token, tipo, nivelAcesso));
    }

    return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas!"));
}

    @GetMapping("/ping")
    public void pong() {

    }

    @Public
    @PostMapping("/recuperar-senha/solicitar")
    public ResponseEntity<?> SolicitaCodigo(@RequestBody @Valid RecuperacaoSolicitacaoDTO dto) {
        usuarioService.solicitarCodigo(dto);
        return ResponseEntity.ok(Map.of("message", "email enviado com sucesso"));
    }

    @Public
    @PostMapping("/recuperar-senha/alterar")
    public ResponseEntity<?> alterarSenha(@RequestBody @Valid RecuperarSenhaDTO dto) {

        usuarioService.alterarSenha(dto);
        return ResponseEntity.ok(
            Map.of("message", "senha alterada com sucesso"));
    }

    private Map<String, Object> loginResponse(Usuario usuario, String token, String tipo, String nivelAcesso) {
        Map<String, Object> usuarioResponse = new LinkedHashMap<>();
        usuarioResponse.put("id", usuario.getId());
        usuarioResponse.put("nome", usuario.getNome());
        usuarioResponse.put("cpf", usuario.getCpf());
        usuarioResponse.put("email", usuario.getEmail());
        usuarioResponse.put("nivelAcesso", nivelAcesso);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("token", token);
        response.put("tipo", tipo);
        response.put("nivelAcesso", nivelAcesso);
        response.put("email", usuario.getEmail());
        response.put("cpf", usuario.getCpf());
        response.put("usuario", usuarioResponse);
        return response;
    }

    private String somenteDigitos(String value) {
        if (value == null) {
            return null;
        }

        return value.replaceAll("\\D", "");
    }
}
