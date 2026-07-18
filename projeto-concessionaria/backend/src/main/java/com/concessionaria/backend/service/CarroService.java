package com.concessionaria.backend.service;

import com.concessionaria.backend.dto.CarroDTO;
import com.concessionaria.backend.model.Carro;
import com.concessionaria.backend.repository.CarroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CarroService {

    private final CarroRepository repository;
    private final AuditoriaService auditoriaService;

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
        Carro salvo = repository.save(carro);

        auditoriaService.registrar(
                "Carro", salvo.getId(), "CRIACAO",
                null, carroParaString(salvo));

        return salvo;
    }

    public Carro atualizar(Long id, CarroDTO dto) {
        Carro carro = buscarPorId(id);
        String dadosAnteriores = carroParaString(carro);

        carro.setMarca(dto.getMarca());
        carro.setModelo(dto.getModelo());
        carro.setAno(dto.getAno());
        carro.setPreco(dto.getPreco());
        carro.setCor(dto.getCor());
        if (dto.getStatus() != null) carro.setStatus(dto.getStatus());
        Carro atualizado = repository.save(carro);

        auditoriaService.registrar(
                "Carro", id, "ATUALIZACAO",
                dadosAnteriores, carroParaString(atualizado));

        return atualizado;
    }

    public void deletar(Long id) {
        Carro carro = buscarPorId(id);
        String dadosAnteriores = carroParaString(carro);
        repository.deleteById(id);

        auditoriaService.registrar(
                "Carro", id, "REMOCAO",
                dadosAnteriores, null);
    }

    public List<Carro> listarPorFaixaDePreco(Double min, Double max) {
        return repository.findByPrecoBetween(min, max);
    }

    public List<Carro> listarPorAno(Integer minAno) {
        return repository.findByAnoGreaterThanEqual(minAno);
    }

    public Map<String, Long> estatisticas() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", repository.count());
        stats.put("disponiveis", repository.countByStatus(Carro.StatusCarro.DISPONIVEL));
        stats.put("vendidos", repository.countByStatus(Carro.StatusCarro.VENDIDO));
        stats.put("reservados", repository.countByStatus(Carro.StatusCarro.RESERVADO));
        return stats;
    }

    private String carroParaString(Carro c) {
        return String.format(
                "Marca:%s | Modelo:%s | Ano:%d | Preco:%.2f | Cor:%s | Status:%s",
                c.getMarca(), c.getModelo(), c.getAno(),
                c.getPreco(), c.getCor(), c.getStatus());
    }
}
