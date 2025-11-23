package com.sys.sysplanner.DTO.response;

import com.sys.sysplanner.domain.enums.Estado;
import lombok.Data;

@Data
public class EnderecoResponse {
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private Estado estado;
    private String cep;
}