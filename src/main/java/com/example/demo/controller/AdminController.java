package com.example.demo.controller;

import java.io.File;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;

@RestController
@RequestMapping({"/admin", "/api/admin"})
public class AdminController {

    private final CheckinRepository checkinRepository;
    private final CheckOutRepository checkOutRepository;

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    public AdminController(CheckinRepository checkinRepository, CheckOutRepository checkOutRepository) {
        this.checkinRepository = checkinRepository;
        this.checkOutRepository = checkOutRepository;
    }

    @GetMapping("/checkins")
    public ResponseEntity<List<Map<String, Object>>> listarCheckins() {
        List<Map<String, Object>> lista = checkinRepository.findAll().stream()
                .map(this::toCheckinResponse).toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/checkouts")
    public ResponseEntity<List<Map<String, Object>>> listarCheckouts() {
        List<Map<String, Object>> lista = checkOutRepository.findAll().stream()
                .map(this::toCheckoutResponse).toList();
        return ResponseEntity.ok(lista);
    }

    @Transactional
    @DeleteMapping("/checkins")
    public ResponseEntity<?> limparCheckins() {
        List<Checkin> todos = checkinRepository.findAll();
        todos.forEach(c -> {
            if (c.getFoto() != null && c.getFoto().getCaminho() != null) {
                new File(c.getFoto().getCaminho()).delete();
            }
        });
        checkinRepository.disableForeignKeyChecks();
        checkinRepository.truncateTable();
        checkinRepository.enableForeignKeyChecks();
        return ResponseEntity.ok(Map.of("message", "Check-ins removidos com sucesso"));
    }

    @Transactional
    @DeleteMapping("/checkouts")
    public ResponseEntity<?> limparCheckouts() {
        List<CheckOut> todos = checkOutRepository.findAll();
        todos.forEach(c -> {
            if (c.getFoto() != null && c.getFoto().getCaminho() != null) {
                new File(c.getFoto().getCaminho()).delete();
            }
        });
        checkOutRepository.disableForeignKeyChecks();
        checkOutRepository.truncateTable();
        checkOutRepository.enableForeignKeyChecks();
        return ResponseEntity.ok(Map.of("message", "Check-outs removidos com sucesso"));
    }

    private Map<String, Object> toCheckinResponse(Checkin checkin) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("id", checkin.getId());
        resp.put("tipo", "Check-in");
        resp.put("createdAt", checkin.getCreatedAt());
        resp.put("posto", toPostoResponse(checkin.getPosto()));
        resp.put("nomeGuarda", checkin.getNomeGuarda());
        resp.put("fotoUrl", extrairUrlFoto(checkin.getFoto()));
        resp.put("temFoto", checkin.getFoto() != null);
        return resp;
    }

    private Map<String, Object> toCheckoutResponse(CheckOut checkout) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("id", checkout.getId());
        resp.put("tipo", "Check-out");
        resp.put("createdAt", checkout.getCreatedAt());
        resp.put("posto", toPostoResponse(checkout.getPosto()));
        resp.put("relatorio", checkout.getRelatorio());                         // total
        resp.put("relatorioMatutino", checkout.getRelatorioMatutino());         // novo
        resp.put("relatorioVespertino", checkout.getRelatorioVespertino());     // novo
        resp.put("incidentesMatutino", checkout.getIncidentesMatutino());
        resp.put("incidentesVespertino", checkout.getIncidentesVespertino());
        resp.put("lesoesPorAguaViva", checkout.getLesoesPorAguaViva());
        resp.put("prevencoesMatutino", checkout.getPrevencoesMatutino());
        resp.put("prevencoesVespertino", checkout.getPrevencoesVespertino());
        resp.put("fotoUrl", extrairUrlFoto(checkout.getFoto()));
        resp.put("temFoto", checkout.getFoto() != null);
        return resp;
    }

    private String extrairUrlFoto(Arquivo arquivo) {
        if (arquivo == null || arquivo.getCaminho() == null) return null;
        String nomeArquivo = arquivo.getCaminho().substring(
                arquivo.getCaminho().lastIndexOf(File.separator) + 1);
        return "/uploads/" + nomeArquivo;
    }

    private Map<String, Object> toPostoResponse(Posto posto) {
        if (posto == null) return null;
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("id", posto.getId());
        resp.put("nome", posto.getNome());
        resp.put("descricao", posto.getDescricao());
        return resp;
    }
}