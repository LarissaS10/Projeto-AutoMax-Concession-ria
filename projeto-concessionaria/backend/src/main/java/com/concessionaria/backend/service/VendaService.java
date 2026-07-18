package com.concessionaria.backend.service;

import com.concessionaria.backend.dto.VendaDTO;
import com.concessionaria.backend.model.Carro;
import com.concessionaria.backend.model.Cliente;
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
    private final AuditoriaService auditoriaService;

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

        Cliente cliente = clienteService.buscarPorId(dto.getClienteId());

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setCarro(carro);
        venda.setValorFinal(dto.getValorFinal() != null ? dto.getValorFinal() : carro.getPreco());

        Venda salva = vendaRepository.save(venda);

        auditoriaService.registrar(
                "Venda", salva.getId(), "CRIACAO",
                null, vendaParaString(salva));

        return salva;
    }

    public List<Venda> listarPorPeriodo(java.time.LocalDateTime inicio,
                                        java.time.LocalDateTime fim) {
        return vendaRepository.findByDataVendaBetween(inicio, fim);
    }

    public List<Venda> listarPorValorMinimo(Double valor) {
        return vendaRepository.findByValorFinalGreaterThanEqual(valor);
    }

    public Double calcularReceitaTotal() {
        Double receita = vendaRepository.calcularReceitaTotal();
        return receita != null ? receita : 0.0;
    }

    public List<Venda> listarVendasMesAtual() {
        return vendaRepository.findVendasMesAtual();
    }

    private String vendaParaString(Venda v) {
        return String.format(
                "Cliente:%s | Carro:%s %s | ValorFinal:%.2f | Data:%s",
                v.getCliente().getNome(),
                v.getCarro().getMarca(), v.getCarro().getModelo(),
                v.getValorFinal(), v.getDataVenda());
    }
}
