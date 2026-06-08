package com.concessionaria.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VendaDTO {

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O ID do carro é obrigatório")
    private Long carroId;

    @Positive(message = "O valor final deve ser positivo")
    private Double valorFinal; //pode ter desconto em relação ao preço do carro
}
