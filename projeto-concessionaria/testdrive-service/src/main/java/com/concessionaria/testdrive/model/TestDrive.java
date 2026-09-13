package com.concessionaria.testdrive.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "test_drives")
public class TestDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O ID do carro é obrigatório")
    @Column(nullable = false)
    private Long carroId;

    @NotBlank(message = "A marca do carro é obrigatória")
    private String carroMarca;

    @NotBlank(message = "O modelo do carro é obrigatório")
    private String carroModelo;

    @NotBlank(message = "O nome do cliente é obrigatório")
    private String clienteNome;

    @NotBlank(message = "O telefone é obrigatório")
    private String clienteTelefone;

    @NotNull(message = "A data agendada é obrigatória")
    private LocalDateTime dataAgendada;

    @Enumerated(EnumType.STRING)
    private StatusTestDrive status = StatusTestDrive.AGENDADO;

    private String observacoes;

    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    public enum StatusTestDrive {
        AGENDADO, REALIZADO, CANCELADO
    }
}