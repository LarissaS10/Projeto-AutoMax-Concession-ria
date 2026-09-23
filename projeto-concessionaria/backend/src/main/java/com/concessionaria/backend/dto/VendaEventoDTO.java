package com.concessionaria.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendaEventoDTO {

    private Long vendaId;
    private Long carroId;
    private String carroMarca;
    private String carroModelo;
    private Long clienteId;
    private String clienteNome;
    private Double valorFinal;
    private LocalDateTime dataVenda;
}