package com.concessionaria.backend;

import com.concessionaria.backend.model.Carro;
import com.concessionaria.backend.model.Cliente;
import com.concessionaria.backend.model.Venda;
import com.concessionaria.backend.repository.CarroRepository;
import com.concessionaria.backend.repository.ClienteRepository;
import com.concessionaria.backend.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VendaRepositoryTest {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente;
    private Carro carro1;
    private Carro carro2;

    @BeforeEach
    void setUp() {
        vendaRepository.deleteAll();
        carroRepository.deleteAll();
        clienteRepository.deleteAll();

        cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setCpf("111.111.111-11");
        cliente = clienteRepository.save(cliente);

        carro1 = new Carro();
        carro1.setMarca("Toyota"); carro1.setModelo("Corolla");
        carro1.setAno(2022); carro1.setPreco(95000.0);
        carro1.setStatus(Carro.StatusCarro.VENDIDO);
        carro1 = carroRepository.save(carro1);

        carro2 = new Carro();
        carro2.setMarca("Honda"); carro2.setModelo("Civic");
        carro2.setAno(2023); carro2.setPreco(120000.0);
        carro2.setStatus(Carro.StatusCarro.VENDIDO);
        carro2 = carroRepository.save(carro2);

        Venda v1 = new Venda();
        v1.setCliente(cliente); v1.setCarro(carro1);
        v1.setValorFinal(90000.0);
        vendaRepository.save(v1);

        Venda v2 = new Venda();
        v2.setCliente(cliente); v2.setCarro(carro2);
        v2.setValorFinal(120000.0);
        vendaRepository.save(v2);
    }

    @Test
    void deveSalvarVenda() {
        assertEquals(2, vendaRepository.count());
    }

    @Test
    void deveListarVendasPorCliente() {
        List<Venda> vendas = vendaRepository.findByClienteId(cliente.getId());
        assertEquals(2, vendas.size());
    }

    @Test
    void deveVerificarSeCarroFoiVendido() {
        assertTrue(vendaRepository.existsByCarroId(carro1.getId()));
    }

    @Test
    void deveListarVendasPorValorMinimo() {
        List<Venda> vendas = vendaRepository.findByValorFinalGreaterThanEqual(100000.0);
        assertEquals(1, vendas.size());
    }

    @Test
    void deveCalcularReceitaTotal() {
        Double receita = vendaRepository.calcularReceitaTotal();
        assertEquals(210000.0, receita);
    }

    @Test
    void deveListarVendasPorPeriodo() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fim = LocalDateTime.now().plusDays(1);
        List<Venda> vendas = vendaRepository.findByDataVendaBetween(inicio, fim);
        assertEquals(2, vendas.size());
    }
}
