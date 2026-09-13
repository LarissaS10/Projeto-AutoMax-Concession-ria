package com.concessionaria.testdrive.service;

import com.concessionaria.testdrive.dto.TestDriveDTO;
import com.concessionaria.testdrive.model.TestDrive;
import com.concessionaria.testdrive.model.TestDrive.StatusTestDrive;
import com.concessionaria.testdrive.repository.TestDriveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestDriveService {

    private final TestDriveRepository repository;

    public List<TestDrive> listarTodos() {
        return repository.findAll();
    }

    public List<TestDrive> listarPorStatus(StatusTestDrive status) {
        return repository.findByStatus(status);
    }

    public List<TestDrive> listarPorCarro(Long carroId) {
        return repository.findByCarroId(carroId);
    }

    public List<TestDrive> listarPorCliente(String nome) {
        return repository.findByClienteNomeContainingIgnoreCase(nome);
    }

    public TestDrive buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test Drive não encontrado: " + id));
    }

    public TestDrive agendar(TestDriveDTO dto) {
        TestDrive testDrive = new TestDrive();
        testDrive.setCarroId(dto.getCarroId());
        testDrive.setCarroMarca(dto.getCarroMarca());
        testDrive.setCarroModelo(dto.getCarroModelo());
        testDrive.setClienteNome(dto.getClienteNome());
        testDrive.setClienteTelefone(dto.getClienteTelefone());
        testDrive.setDataAgendada(dto.getDataAgendada());
        testDrive.setObservacoes(dto.getObservacoes());
        testDrive.setStatus(StatusTestDrive.AGENDADO);
        return repository.save(testDrive);
    }

    public TestDrive realizarTestDrive(Long id) {
        TestDrive testDrive = buscarPorId(id);
        if (testDrive.getStatus() != StatusTestDrive.AGENDADO) {
            throw new RuntimeException("Test Drive não está com status AGENDADO!");
        }
        testDrive.setStatus(StatusTestDrive.REALIZADO);
        return repository.save(testDrive);
    }

    public TestDrive cancelar(Long id) {
        TestDrive testDrive = buscarPorId(id);
        if (testDrive.getStatus() == StatusTestDrive.REALIZADO) {
            throw new RuntimeException("Não é possível cancelar um Test Drive já realizado!");
        }
        testDrive.setStatus(StatusTestDrive.CANCELADO);
        return repository.save(testDrive);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    public Map<String, Long> estatisticas() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", repository.count());
        stats.put("agendados", repository.countByStatus(StatusTestDrive.AGENDADO));
        stats.put("realizados", repository.countByStatus(StatusTestDrive.REALIZADO));
        stats.put("cancelados", repository.countByStatus(StatusTestDrive.CANCELADO));
        return stats;
    }
}
