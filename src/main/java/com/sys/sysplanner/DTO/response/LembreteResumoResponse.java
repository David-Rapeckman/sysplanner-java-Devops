package com.sys.sysplanner.DTO.response;

import com.sys.sysplanner.domain.enums.Categoria;
import com.sys.sysplanner.domain.enums.Prioridade;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LembreteResumoResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime data;
    private Prioridade prioridade;
    private Categoria categoria;
    private String concluido;
}
