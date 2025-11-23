package com.sys.sysplanner.DTO.request;

import com.sys.sysplanner.domain.enums.Categoria;
import com.sys.sysplanner.domain.enums.Prioridade;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LembreteCreateRequest {

    private String titulo;
    private String descricao;
    private LocalDateTime data;
    private Prioridade prioridade;
    private Categoria categoria;
    private Long usuarioId;
}
