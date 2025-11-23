package com.sys.sysplanner.DTO.response;

import com.sys.sysplanner.domain.enums.Role;
import lombok.Data;

@Data
public class UsuarioResponse {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private Role role;
    private EnderecoResponse endereco;
}