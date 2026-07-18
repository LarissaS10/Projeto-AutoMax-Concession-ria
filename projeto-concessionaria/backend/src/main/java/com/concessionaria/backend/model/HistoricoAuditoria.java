package com.concessionaria.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historico_auditoria")
public class HistoricoAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String entidade; //"Carro", "Cliente", "Venda"

    @Column(nullable = false)
    private Long entidadeId; //ID do registro alterado

    @Column(nullable = false)
    private String operacao; //"CRIACAO", "ATUALIZACAO", "REMOCAO"

    @Column(length = 2000)
    private String dadosAnteriores; //como estava antes

    @Column(length = 2000)
    private String dadosNovos; //como ficou depois

    private LocalDateTime dataHora;

    @PrePersist
    public void prePersist() {
        this.dataHora = LocalDateTime.now();
    }
}
