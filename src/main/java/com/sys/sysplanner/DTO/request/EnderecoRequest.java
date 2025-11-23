package com.sys.sysplanner.DTO.request;

import com.sys.sysplanner.domain.enums.Estado;
import lombok.Data;

@Data
public class EnderecoRequest {
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private Estado estado;
    private String cep;
}
