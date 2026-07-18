package com.concessionaria.backend.controller;

import com.concessionaria.backend.model.HistoricoAuditoria;
import com.concessionaria.backend.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuditoriaController {

    private final AuditoriaService service;

    @GetMapping
    public ResponseEntity<List<HistoricoAuditoria>> listarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/entidade/{entidade}")
    public ResponseEntity<List<HistoricoAuditoria>> listarPorEntidade(
            @PathVariable String entidade) {
        return ResponseEntity.ok(service.buscarPorEntidade(entidade));
    }

    @GetMapping("/entidade/{entidade}/{id}")
    public ResponseEntity<List<HistoricoAuditoria>> listarPorEntidadeEId(
            @PathVariable String entidade, @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorEntidadeEId(entidade, id));
    }
}
