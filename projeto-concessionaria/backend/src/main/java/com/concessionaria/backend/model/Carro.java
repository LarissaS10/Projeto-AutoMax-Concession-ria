package com.concessionaria.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carros")
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A marca é obrigatória")
    private String marca;

    @NotBlank(message = "O modelo é obrigatório")
    private String modelo;

    @NotNull(message = "O ano é obrigatório")
    @Min(value = 1900, message = "Ano inválido")
    private Integer ano;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser positivo")
    private Double preco;

    private String cor;

    @Enumerated(EnumType.STRING)
    private StatusCarro status = StatusCarro.DISPONIVEL;

    public enum StatusCarro {
        DISPONIVEL, VENDIDO, RESERVADO
    }
}