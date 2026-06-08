package com.concessionaria.backend.service;

import com.concessionaria.backend.dto.VendaDTO;
import com.concessionaria.backend.model.Carro;
import com.concessionaria.backend.model.Venda;
import com.concessionaria.backend.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final CarroService carroService;
    private final ClienteService clienteService;

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public List<Venda> listarPorCliente(Long clienteId) {
        return vendaRepository.findByClienteId(clienteId);
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada: " + id));
    }

    public Venda realizar(VendaDTO dto) {
        Carro carro = carroService.buscarPorId(dto.getCarroId());

        //regra de negócio: só pode vender carro disponível
        if (carro.getStatus() != Carro.StatusCarro.DISPONIVEL) {
            throw new RuntimeException("Carro não está disponível para venda!");
        }

        //marca o carro como vendido
        carro.setStatus(Carro.StatusCarro.VENDIDO);

        Venda venda = new Venda();
        venda.setCliente(clienteService.buscarPorId(dto.getClienteId()));
        venda.setCarro(carro);

        //se não informar o valor, ele usa o preço do carro
        venda.setValorFinal(dto.getValorFinal() != null ? dto.getValorFinal() : carro.getPreco());

        return vendaRepository.save(venda);
    }
}
