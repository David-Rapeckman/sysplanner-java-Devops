package com.sys.sysplanner.DTO.request;

import com.sys.sysplanner.domain.enums.Role;
import lombok.Data;

@Data
public class UsuarioCreateRequest {
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private EnderecoRequest endereco;
}
