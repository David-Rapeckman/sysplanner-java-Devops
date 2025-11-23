package com.sys.sysplanner.DTO.request;

import com.sys.sysplanner.domain.enums.Estado;
import lombok.Data;

@Data
public class UsuarioAdminUpdateRequest {

    private String nome;
    private String email;

    private String senha;           // opcional
    private String confirmarSenha;  // opcional

    private String rua;
    private String numero;
    private String bairro;
    private String complemento;
    private String cidade;
    private Estado estado;
    private String cep;

    private String role; // opcional (ADMIN/USER)
}