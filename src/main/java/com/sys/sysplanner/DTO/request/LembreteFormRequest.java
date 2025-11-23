package com.sys.sysplanner.DTO.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LembreteFormRequest {
    private Long id; // para identificar edição
    private Long usuarioId;
    private String titulo;
    private String descricao;
    private String prioridade; // ALTA, MODERADA, BAIXA
    private LocalDateTime data;
}
