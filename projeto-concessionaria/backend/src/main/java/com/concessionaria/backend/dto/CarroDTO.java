package com.concessionaria.backend.dto;

import com.concessionaria.backend.model.Carro;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CarroDTO {

    @NotBlank(message = "A marca é obrigatória")
    private String marca;

    @NotBlank(message = "O modelo é obrigatório")
    private String modelo;

    @NotNull(message = "O ano é obrigatório")
    @Min(1900)
    private Integer ano;

    @NotNull(message = "O preço é obrigatório")
    @Positive
    private Double preco;

    private String cor;

    private Carro.StatusCarro status;
}
