package com.concessionaria.backend.service;

import com.concessionaria.backend.dto.ClienteDTO;
import com.concessionaria.backend.model.Cliente;
import com.concessionaria.backend.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
    private final AuditoriaService auditoriaService;

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + id));
    }

    public Cliente cadastrar(ClienteDTO dto) {
        if (repository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + dto.getCpf());
        }
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        Cliente salvo = repository.save(cliente);

        auditoriaService.registrar(
                "Cliente", salvo.getId(), "CRIACAO",
                null, clienteParaString(salvo));

        return salvo;
    }

    public Cliente atualizar(Long id, ClienteDTO dto) {
        Cliente cliente = buscarPorId(id);
        String dadosAnteriores = clienteParaString(cliente);

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        Cliente atualizado = repository.save(cliente);

        auditoriaService.registrar(
                "Cliente", id, "ATUALIZACAO",
                dadosAnteriores, clienteParaString(atualizado));

        return atualizado;
    }

    public void deletar(Long id) {
        Cliente cliente = buscarPorId(id);
        String dadosAnteriores = clienteParaString(cliente);
        repository.deleteById(id);

        auditoriaService.registrar(
                "Cliente", id, "REMOCAO",
                dadosAnteriores, null);
    }

    private String clienteParaString(Cliente c) {
        return String.format(
                "Nome:%s | CPF:%s | Email:%s | Telefone:%s",
                c.getNome(), c.getCpf(), c.getEmail(), c.getTelefone());
    }
}
