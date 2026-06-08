package com.concessionaria.backend.service;

import com.concessionaria.backend.dto.CarroDTO;
import com.concessionaria.backend.model.Carro;
import com.concessionaria.backend.repository.CarroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarroService {

    private final CarroRepository repository;

    public List<Carro> listarTodos() {
        return repository.findAll();
    }

    public List<Carro> listarDisponiveis() {
        return repository.findByStatus(Carro.StatusCarro.DISPONIVEL);
    }

    public List<Carro> listarPorMarca(String marca) {
        return repository.findByMarcaIgnoreCase(marca);
    }

    public Carro buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado: " + id));
    }

    public Carro cadastrar(CarroDTO dto) {
        Carro carro = new Carro();
        carro.setMarca(dto.getMarca());
        carro.setModelo(dto.getModelo());
        carro.setAno(dto.getAno());
        carro.setPreco(dto.getPreco());
        carro.setCor(dto.getCor());
        carro.setStatus(dto.getStatus() != null ? dto.getStatus() : Carro.StatusCarro.DISPONIVEL);
        return repository.save(carro);
    }

    public Carro atualizar(Long id, CarroDTO dto) {
        Carro carro = buscarPorId(id);
        carro.setMarca(dto.getMarca());
        carro.setModelo(dto.getModelo());
        carro.setAno(dto.getAno());
        carro.setPreco(dto.getPreco());
        carro.setCor(dto.getCor());
        if (dto.getStatus() != null) carro.setStatus(dto.getStatus());
        return repository.save(carro);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
