package com.sys.sysplanner.domain;

import com.sys.sysplanner.domain.enums.*;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Embeddable
public class Endereco {

    @NotBlank(message = "A rua é obrigatória")
    @Size(max = 150, message = "A rua deve ter no máximo 150 caracteres")
    private String rua;

    @NotBlank(message = "O número é obrigatório")
    @Size(max = 20, message = "O número deve ter no máximo 20 caracteres")
    private String numero;

    @Size(max = 60, message = "O complemento deve ter no máximo 60 caracteres")
    private String complemento;

    @NotBlank(message = "O bairro é obrigatório")
    @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
    private String cidade;

    @NotNull(message = "O estado é obrigatório")
    private Estado estado;

    @NotBlank(message = "O CEP é obrigatório")
    @Size(min = 8, max = 8, message = "O CEP deve ter 8 dígitos")
    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter apenas números")
    private String cep;
}

