package com.concessionaria.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //muitas vendas podem ter um cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    //um carro só pode ser vendido uma vez
    @OneToOne
    @JoinColumn(name = "carro_id", nullable = false)
    private Carro carro;

    private Double valorFinal;

    private LocalDateTime dataVenda;

    @PrePersist
    public void prePersist() {
        this.dataVenda = LocalDateTime.now();
    }
}
