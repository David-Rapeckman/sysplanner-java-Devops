package com.sys.sysplanner.DTO.request;

import com.sys.sysplanner.domain.enums.*;
import lombok.Data;

@Data
public class UsuarioUpdateRequest {
    private String nome;
    private String email;
    private String senha;
    private EnderecoRequest endereco;
}