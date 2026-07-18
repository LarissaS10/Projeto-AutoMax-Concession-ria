package com.concessionaria.backend;

import com.concessionaria.backend.model.Cliente;
import com.concessionaria.backend.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        Cliente c1 = new Cliente();
        c1.setNome("João Silva");
        c1.setCpf("111.111.111-11");
        c1.setEmail("joao@email.com");
        c1.setTelefone("51999990001");

        Cliente c2 = new Cliente();
        c2.setNome("Maria Souza");
        c2.setCpf("222.222.222-22");
        c2.setEmail("maria@email.com");
        c2.setTelefone("51999990002");

        Cliente c3 = new Cliente();
        c3.setNome("Carlos Lima");
        c3.setCpf("333.333.333-33");
        c3.setEmail("carlos@email.com");
        c3.setTelefone("51999990003");

        repository.save(c1);
        repository.save(c2);
        repository.save(c3);
    }

    @Test
    void deveSalvarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Ana Paula");
        cliente.setCpf("444.444.444-44");
        Cliente salvo = repository.save(cliente);
        assertNotNull(salvo.getId());
    }

    @Test
    void deveListarTodos() {
        List<Cliente> todos = repository.findAll();
        assertEquals(3, todos.size());
    }

    @Test
    void deveBuscarPorCpf() {
        assertTrue(repository.findByCpf("111.111.111-11").isPresent());
    }

    @Test
    void deveRetornarVazioParaCpfInexistente() {
        assertTrue(repository.findByCpf("999.999.999-99").isEmpty());
    }

    @Test
    void deveVerificarExistenciaPorCpf() {
        assertTrue(repository.existsByCpf("222.222.222-22"));
        assertFalse(repository.existsByCpf("999.999.999-99"));
    }

    @Test
    void deveBuscarPorId() {
        Cliente cliente = new Cliente();
        cliente.setNome("Pedro Costa");
        cliente.setCpf("555.555.555-55");
        Cliente salvo = repository.save(cliente);
        assertTrue(repository.findById(salvo.getId()).isPresent());
    }

    @Test
    void deveDeletarCliente() {
        List<Cliente> todos = repository.findAll();
        repository.deleteById(todos.get(0).getId());
        assertEquals(2, repository.count());
    }

    @Test
    void naoDeveSalvarDoisClientesComMesmoCpf() {
        Cliente duplicado = new Cliente();
        duplicado.setNome("Outro Nome");
        duplicado.setCpf("111.111.111-11"); //CPF já cadastrado
        assertThrows(Exception.class, () -> repository.saveAndFlush(duplicado));
    }
}
