package com.concessionaria.backend.controller;

import com.concessionaria.backend.dto.VendaDTO;
import com.concessionaria.backend.model.Venda;
import com.concessionaria.backend.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class VendaController {

    private final VendaService service;

    @GetMapping
    public ResponseEntity<List<Venda>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venda>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.listarPorCliente(clienteId));
    }

    @PostMapping
    public ResponseEntity<Venda> realizar(@Valid @RequestBody VendaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.realizar(dto));
    }
}