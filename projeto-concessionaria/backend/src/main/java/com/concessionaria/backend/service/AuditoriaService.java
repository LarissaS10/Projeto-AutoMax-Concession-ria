package com.concessionaria.backend.service;

import com.concessionaria.backend.model.HistoricoAuditoria;
import com.concessionaria.backend.repository.HistoricoAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final HistoricoAuditoriaRepository repository;

    //registra no histórico
    public void registrar(String entidade, Long entidadeId, String operacao, String dadosAnteriores, String dadosNovos) {
        HistoricoAuditoria historico = new HistoricoAuditoria();
        historico.setEntidade(entidade);
        historico.setEntidadeId(entidadeId);
        historico.setOperacao(operacao);
        historico.setDadosAnteriores(dadosAnteriores);
        historico.setDadosNovos(dadosNovos);
        repository.save(historico);
    }

    public List<HistoricoAuditoria> buscarPorEntidade(String entidade) {
        return repository.findByEntidadeOrderByDataHoraDesc(entidade);
    }

    public List<HistoricoAuditoria> buscarPorEntidadeEId(String entidade, Long id) {
        return repository.findByEntidadeAndEntidadeIdOrderByDataHoraDesc(entidade, id);
    }

    public List<HistoricoAuditoria> buscarTodos() {
        return repository.findAll();
    }
}
