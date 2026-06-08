package com.concessionaria.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClienteDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @Email(message = "E-mail inválido")
    private String email;

    private String telefone;
}
