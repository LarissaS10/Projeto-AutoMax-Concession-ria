package com.concessionaria.testdrive;

import com.concessionaria.testdrive.model.TestDrive;
import com.concessionaria.testdrive.model.TestDrive.StatusTestDrive;
import com.concessionaria.testdrive.repository.TestDriveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestDriveRepositoryTest {

    @Autowired
    private TestDriveRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        TestDrive td1 = new TestDrive();
        td1.setCarroId(1L);
        td1.setCarroMarca("Toyota"); td1.setCarroModelo("Corolla");
        td1.setClienteNome("Apolo"); td1.setClienteTelefone("51999990001");
        td1.setDataAgendada(LocalDateTime.now().plusDays(1));
        td1.setStatus(StatusTestDrive.AGENDADO);

        TestDrive td2 = new TestDrive();
        td2.setCarroId(2L);
        td2.setCarroMarca("Honda"); td2.setCarroModelo("Civic");
        td2.setClienteNome("Mellany"); td2.setClienteTelefone("51999990002");
        td2.setDataAgendada(LocalDateTime.now().plusDays(2));
        td2.setStatus(StatusTestDrive.REALIZADO);

        TestDrive td3 = new TestDrive();
        td3.setCarroId(1L);
        td3.setCarroMarca("Toyota"); td3.setCarroModelo("Hilux");
        td3.setClienteNome("Perola"); td3.setClienteTelefone("51999990003");
        td3.setDataAgendada(LocalDateTime.now().plusDays(3));
        td3.setStatus(StatusTestDrive.CANCELADO);

        repository.save(td1);
        repository.save(td2);
        repository.save(td3);
    }

    @Test
    void deveSalvarTestDrive() {
        TestDrive td = new TestDrive();
        td.setCarroId(3L);
        td.setCarroMarca("Fiat"); td.setCarroModelo("Pulse");
        td.setClienteNome("Larissa"); td.setClienteTelefone("51999990004");
        td.setDataAgendada(LocalDateTime.now().plusDays(4));
        TestDrive salvo = repository.save(td);
        assertNotNull(salvo.getId());
    }

    @Test
    void deveListarPorStatus() {
        List<TestDrive> agendados = repository.findByStatus(StatusTestDrive.AGENDADO);
        assertEquals(1, agendados.size());
    }

    @Test
    void deveListarPorCarroId() {
        List<TestDrive> porCarro = repository.findByCarroId(1L);
        assertEquals(2, porCarro.size());
    }

    @Test
    void deveListarPorNomeCliente() {
        List<TestDrive> resultado = repository.findByClienteNomeContainingIgnoreCase("apolo");
        assertEquals(1, resultado.size());
    }

    @Test
    void deveContarPorStatus() {
        assertEquals(1, repository.countByStatus(StatusTestDrive.AGENDADO));
        assertEquals(1, repository.countByStatus(StatusTestDrive.REALIZADO));
        assertEquals(1, repository.countByStatus(StatusTestDrive.CANCELADO));
    }

    @Test
    void deveListarPorPeriodo() {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = LocalDateTime.now().plusDays(5);
        List<TestDrive> resultado = repository.findByDataAgendadaBetween(inicio, fim);
        assertEquals(3, resultado.size());
    }

    @Test
    void deveDeletarTestDrive() {
        List<TestDrive> todos = repository.findAll();
        repository.deleteById(todos.get(0).getId());
        assertEquals(2, repository.count());
    }
}