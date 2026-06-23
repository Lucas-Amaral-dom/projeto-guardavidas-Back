package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Arquivo;
import com.example.demo.entity.CheckOut;
import com.example.demo.entity.Checkin;
import com.example.demo.entity.Posto;
import com.example.demo.repository.ArquivoRepository;
import com.example.demo.repository.CheckOutRepository;
import com.example.demo.repository.CheckinRepository;
import com.example.demo.repository.PostoRepository;

@RestController
public class CheckController {

    @Autowired
    private CheckinRepository checkinRepository;

    @Autowired
    private CheckOutRepository checkoutRepository;

    @Autowired
    private PostoRepository postoRepository;

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    // ────────────────────────── CHECK‑IN ──────────────────────────
    @PostMapping("/checkin")
    public ResponseEntity<?> checkin(
            @RequestParam("postoId") Long postoId,
            @RequestParam("postoNome") String postoNome,   // compatibilidade, não usado diretamente
            @RequestParam("nomeGuarda") String nomeGuarda,
            @RequestParam("foto") MultipartFile fotoFile) {

        try {
            Posto posto = postoRepository.findById(postoId)
                    .orElseThrow(() -> new RuntimeException("Posto não encontrado: " + postoId));

            Arquivo arquivo = salvarArquivo(fotoFile);

            Checkin checkin = new Checkin();
            checkin.setPosto(posto);
            checkin.setFoto(arquivo);
            checkin.setNomeGuarda(nomeGuarda);

            checkin = checkinRepository.save(checkin);

            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("id", checkin.getId());
            resp.put("fotoUrl", "/uploads/" + arquivo.getCaminho().substring(arquivo.getCaminho().lastIndexOf(File.separator) + 1));
            resp.put("message", "Check-in registrado com sucesso");
            return ResponseEntity.ok(resp);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao salvar foto: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao registrar check-in: " + e.getMessage()));
        }
    }

    // ────────────────────────── CHECK‑OUT ─────────────────────────
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @RequestParam("postoId") Long postoId,
            @RequestParam("postoNome") String postoNome,
            @RequestParam("relatorioMatutino") int relatorioMatutino,       // novo
            @RequestParam("relatorioVespertino") int relatorioVespertino,   // novo
            @RequestParam("relatorio") int relatorio,                       // total
            @RequestParam("incidentesMatutino") int incidentesMatutino,
            @RequestParam("incidentesVespertino") int incidentesVespertino,
            @RequestParam("lesoesPorAguaViva") int lesoesPorAguaViva,
            @RequestParam("prevencoesMatutino") int prevencoesMatutino,
            @RequestParam("prevencoesVespertino") int prevencoesVespertino,
            @RequestParam("foto") MultipartFile fotoFile) {

        try {
            Posto posto = postoRepository.findById(postoId)
                    .orElseThrow(() -> new RuntimeException("Posto não encontrado: " + postoId));

            Arquivo arquivo = salvarArquivo(fotoFile);

            CheckOut checkout = new CheckOut();
            checkout.setPosto(posto);
            checkout.setFoto(arquivo);
            checkout.setRelatorio(relatorio);
            checkout.setRelatorioMatutino(relatorioMatutino);
            checkout.setRelatorioVespertino(relatorioVespertino);
            checkout.setIncidentesMatutino(incidentesMatutino);
            checkout.setIncidentesVespertino(incidentesVespertino);
            checkout.setLesoesPorAguaViva(lesoesPorAguaViva);
            checkout.setPrevencoesMatutino(prevencoesMatutino);
            checkout.setPrevencoesVespertino(prevencoesVespertino);

            checkout = checkoutRepository.save(checkout);

            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("id", checkout.getId());
            resp.put("fotoUrl", "/uploads/" + arquivo.getCaminho().substring(arquivo.getCaminho().lastIndexOf(File.separator) + 1));
            resp.put("message", "Check-out registrado com sucesso");
            return ResponseEntity.ok(resp);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao salvar foto: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao registrar check-out: " + e.getMessage()));
        }
    }

    // ── Método auxiliar para salvar o arquivo e criar a entidade Arquivo ──
    private Arquivo salvarArquivo(MultipartFile fotoFile) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String nomeOriginal = fotoFile.getOriginalFilename();
        String extensao = (nomeOriginal != null && nomeOriginal.contains("."))
                ? nomeOriginal.substring(nomeOriginal.lastIndexOf("."))
                : ".jpg";
        String nomeUnico = UUID.randomUUID() + extensao;
        Path caminhoCompleto = Path.of(uploadDir, nomeUnico);

        Files.copy(fotoFile.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

        Arquivo arquivo = new Arquivo();
        arquivo.setNome(nomeOriginal != null ? nomeOriginal : nomeUnico);
        arquivo.setTipo(fotoFile.getContentType());
        arquivo.setTamanho(fotoFile.getSize());
        arquivo.setCaminho(caminhoCompleto.toString());

        return arquivoRepository.save(arquivo);
    }
}