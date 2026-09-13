package com.concessionaria.testdrive.controller;

import com.concessionaria.testdrive.dto.TestDriveDTO;
import com.concessionaria.testdrive.model.TestDrive;
import com.concessionaria.testdrive.model.TestDrive.StatusTestDrive;
import com.concessionaria.testdrive.service.TestDriveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/testdrive")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TestDriveController {

    private final TestDriveService service;

    //GET /api/testdrive (lista todos)
    @GetMapping
    public ResponseEntity<List<TestDrive>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    //GET /api/testdrive/status?status=AGENDADO
    @GetMapping("/status")
    public ResponseEntity<List<TestDrive>> listarPorStatus(
            @RequestParam StatusTestDrive status) {
        return ResponseEntity.ok(service.listarPorStatus(status));
    }

    //GET /api/testdrive/carro/1
    @GetMapping("/carro/{carroId}")
    public ResponseEntity<List<TestDrive>> listarPorCarro(
            @PathVariable Long carroId) {
        return ResponseEntity.ok(service.listarPorCarro(carroId));
    }

    //GET /api/testdrive/cliente?nome=João
    @GetMapping("/cliente")
    public ResponseEntity<List<TestDrive>> listarPorCliente(
            @RequestParam String nome) {
        return ResponseEntity.ok(service.listarPorCliente(nome));
    }

    //GET /api/testdrive/1
    @GetMapping("/{id}")
    public ResponseEntity<TestDrive> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    //GET /api/testdrive/estatisticas
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Long>> estatisticas() {
        return ResponseEntity.ok(service.estatisticas());
    }

    //POST /api/testdrive (agendar)
    @PostMapping
    public ResponseEntity<TestDrive> agendar(
            @Valid @RequestBody TestDriveDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.agendar(dto));
    }

    //PATCH /api/testdrive/1/realizar
    @PatchMapping("/{id}/realizar")
    public ResponseEntity<TestDrive> realizar(@PathVariable Long id) {
        return ResponseEntity.ok(service.realizarTestDrive(id));
    }

    //PATCH /api/testdrive/1/cancelar
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<TestDrive> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    //DELETE /api/testdrive/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}