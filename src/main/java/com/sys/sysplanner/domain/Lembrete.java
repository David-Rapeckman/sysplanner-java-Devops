package com.sys.sysplanner.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.sys.sysplanner.domain.enums.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lembretes")
public class Lembrete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{lembrete.titulo.notblank}")
    @Size(max = 100, message = "{lembrete.titulo.size}")
    private String titulo;

    @Size(max = 500, message = "{lembrete.descricao.size}")
    private String descricao;

    @NotNull(message = "{lembrete.data.notnull}")
    private LocalDateTime data;

    @NotNull(message = "{lembrete.prioridade.notnull}")
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @NotNull(message = "{lembrete.categoria.notnull}")
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @NotBlank(message = "{lembrete.concluido.notblank}")
    @Builder.Default
    private String concluido = "N";

    @NotNull(message = "{lembrete.usuario.notnull}")
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
