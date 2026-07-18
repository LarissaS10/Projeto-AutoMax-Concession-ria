package com.concessionaria.backend;

import com.concessionaria.backend.model.Carro;
import com.concessionaria.backend.repository.CarroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
public class CarroRepositoryTest {

    @Autowired
    private CarroRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        Carro c1 = new Carro();
        c1.setMarca("Toyota"); c1.setModelo("Corolla");
        c1.setAno(2022); c1.setPreco(95000.0); c1.setCor("Prata");

        Carro c2 = new Carro();
        c2.setMarca("Honda"); c2.setModelo("Civic");
        c2.setAno(2021); c2.setPreco(110000.0); c2.setCor("Preto");
        c2.setStatus(Carro.StatusCarro.VENDIDO);

        Carro c3 = new Carro();
        c3.setMarca("Toyota"); c3.setModelo("Hilux");
        c3.setAno(2023); c3.setPreco(250000.0); c3.setCor("Branco");

        repository.save(c1);
        repository.save(c2);
        repository.save(c3);
    }

    @Test
    void deveSalvarCarro() {
        Carro carro = new Carro();
        carro.setMarca("Fiat"); carro.setModelo("Pulse");
        carro.setAno(2023); carro.setPreco(85000.0);
        Carro salvo = repository.save(carro);
        assertNotNull(salvo.getId());
    }

    @Test
    void deveListarCarrosDisponiveis() {
        List<Carro> disponiveis = repository.findByStatus(Carro.StatusCarro.DISPONIVEL);
        assertEquals(2, disponiveis.size());
    }

    @Test
    void deveListarPorMarca() {
        List<Carro> toyotas = repository.findByMarcaIgnoreCase("toyota");
        assertEquals(2, toyotas.size());
    }

    @Test
    void deveListarPorFaixaDePreco() {
        List<Carro> carros = repository.findByPrecoBetween(90000.0, 120000.0);
        assertEquals(2, carros.size());
    }

    @Test
    void deveListarPorAnoMinimo() {
        List<Carro> carros = repository.findByAnoGreaterThanEqual(2022);
        assertEquals(2, carros.size());
    }

    @Test
    void deveContarPorStatus() {
        long total = repository.countByStatus(Carro.StatusCarro.DISPONIVEL);
        assertEquals(2, total);
    }

    @Test
    void deveBuscarPorId() {
        Carro carro = new Carro();
        carro.setMarca("VW"); carro.setModelo("Golf");
        carro.setAno(2022); carro.setPreco(120000.0);
        Carro salvo = repository.save(carro);
        assertTrue(repository.findById(salvo.getId()).isPresent());
    }

    @Test
    void deveDeletarCarro() {
        List<Carro> todos = repository.findAll();
        repository.deleteById(todos.get(0).getId());
        assertEquals(2, repository.count());
    }
}
