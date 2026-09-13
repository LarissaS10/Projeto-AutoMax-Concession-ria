package com.concessionaria.testdrive.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TestDriveDTO {

    @NotNull(message = "O ID do carro é obrigatório")
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

    private String observacoes;
}