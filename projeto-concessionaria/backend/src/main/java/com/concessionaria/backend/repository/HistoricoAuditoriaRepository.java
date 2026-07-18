package com.concessionaria.backend.repository;

import com.concessionaria.backend.model.HistoricoAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoAuditoriaRepository extends JpaRepository<HistoricoAuditoria, Long> {

    List<HistoricoAuditoria> findByEntidadeOrderByDataHoraDesc(String entidade);

    List<HistoricoAuditoria> findByEntidadeAndEntidadeIdOrderByDataHoraDesc(
            String entidade, Long entidadeId);

    List<HistoricoAuditoria> findByOperacaoOrderByDataHoraDesc(String operacao);
}
