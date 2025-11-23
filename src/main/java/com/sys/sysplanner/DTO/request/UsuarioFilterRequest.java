package com.sys.sysplanner.DTO.request;

import lombok.Data;

@Data
public class UsuarioFilterRequest {
    private String nome;
    private String email;
    private String cpf;
    private String estado; // do Endereco
    private String cidade; // do Endereco
}