package com.concessionaria.backend;

import com.concessionaria.backend.model.HistoricoAuditoria;
import com.concessionaria.backend.repository.HistoricoAuditoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class HistoricoAuditoriaRepositoryTest {

    @Autowired
    private HistoricoAuditoriaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        HistoricoAuditoria h1 = new HistoricoAuditoria();
        h1.setEntidade("Carro"); h1.setEntidadeId(1L);
        h1.setOperacao("CRIACAO"); h1.setDadosNovos("Marca:Toyota");

        HistoricoAuditoria h2 = new HistoricoAuditoria();
        h2.setEntidade("Carro"); h2.setEntidadeId(1L);
        h2.setOperacao("ATUALIZACAO");
        h2.setDadosAnteriores("Marca:Toyota");
        h2.setDadosNovos("Marca:Toyota | Cor:Prata");

        HistoricoAuditoria h3 = new HistoricoAuditoria();
        h3.setEntidade("Cliente"); h3.setEntidadeId(1L);
        h3.setOperacao("CRIACAO"); h3.setDadosNovos("Nome:João");

        HistoricoAuditoria h4 = new HistoricoAuditoria();
        h4.setEntidade("Venda"); h4.setEntidadeId(1L);
        h4.setOperacao("CRIACAO"); h4.setDadosNovos("ValorFinal:95000");

        repository.save(h1);
        repository.save(h2);
        repository.save(h3);
        repository.save(h4);
    }

    @Test
    void deveSalvarHistorico() {
        assertEquals(4, repository.count());
    }

    @Test
    void deveListarPorEntidade() {
        List<HistoricoAuditoria> carros =
                repository.findByEntidadeOrderByDataHoraDesc("Carro");
        assertEquals(2, carros.size());
    }

    @Test
    void deveListarPorEntidadeEId() {
        List<HistoricoAuditoria> historico =
                repository.findByEntidadeAndEntidadeIdOrderByDataHoraDesc("Carro", 1L);
        assertEquals(2, historico.size());
    }

    @Test
    void deveListarPorOperacao() {
        List<HistoricoAuditoria> criacoes =
                repository.findByOperacaoOrderByDataHoraDesc("CRIACAO");
        assertEquals(3, criacoes.size());
    }

    @Test
    void deveRetornarHistoricoOrdenadoPorDataDesc() {
        List<HistoricoAuditoria> historico =
                repository.findByEntidadeOrderByDataHoraDesc("Carro");
        assertEquals(2, historico.size());

        assertTrue(historico.stream()
                .anyMatch(h -> h.getOperacao().equals("CRIACAO")));
        assertTrue(historico.stream()
                .anyMatch(h -> h.getOperacao().equals("ATUALIZACAO")));
    }

    @Test
    void deveListarHistoricoDeCliente() {
        List<HistoricoAuditoria> clientes =
                repository.findByEntidadeOrderByDataHoraDesc("Cliente");
        assertEquals(1, clientes.size());
    }

    @Test
    void deveListarHistoricoDeVenda() {
        List<HistoricoAuditoria> vendas =
                repository.findByEntidadeOrderByDataHoraDesc("Venda");
        assertEquals(1, vendas.size());
    }
}
