package com.sys.sysplanner.mapper;

import com.sys.sysplanner.DTO.request.LembreteCreateRequest;
import com.sys.sysplanner.DTO.request.LembreteUpdateRequest;
import com.sys.sysplanner.DTO.response.LembreteResponse;
import com.sys.sysplanner.DTO.response.LembreteResumoResponse;
import com.sys.sysplanner.domain.Lembrete;
import com.sys.sysplanner.domain.Usuario;
import org.springframework.stereotype.Component;

@Component
public class LembreteMapper {

    private final UsuarioMapper usuarioMapper;

    public LembreteMapper(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    public Lembrete toEntity(LembreteCreateRequest dto, Usuario usuario) {
        if (dto == null || usuario == null) return null;

        Lembrete lembrete = new Lembrete();
        lembrete.setTitulo(dto.getTitulo());
        lembrete.setDescricao(dto.getDescricao());
        lembrete.setData(dto.getData());
        lembrete.setPrioridade(dto.getPrioridade());
        lembrete.setCategoria(dto.getCategoria());
        lembrete.setConcluido("N");
        lembrete.setUsuario(usuario);

        return lembrete;
    }

    public Lembrete toEntity(LembreteUpdateRequest dto, Lembrete lembrete) {
        if (dto == null || lembrete == null) return null;

        lembrete.setTitulo(dto.getTitulo());
        lembrete.setDescricao(dto.getDescricao());
        lembrete.setData(dto.getData());
        lembrete.setPrioridade(dto.getPrioridade());
        lembrete.setCategoria(dto.getCategoria());
        lembrete.setConcluido(dto.getConcluido());

        return lembrete;
    }

    public LembreteResponse toResponse(Lembrete lembrete) {
        if (lembrete == null) return null;

        LembreteResponse response = new LembreteResponse();
        response.setId(lembrete.getId());
        response.setTitulo(lembrete.getTitulo());
        response.setDescricao(lembrete.getDescricao());
        response.setData(lembrete.getData());
        response.setPrioridade(lembrete.getPrioridade());
        response.setCategoria(lembrete.getCategoria());
        response.setConcluido(lembrete.getConcluido());
        response.setUsuario(usuarioMapper.toResponse(lembrete.getUsuario()));

        return response;
    }

    public LembreteResumoResponse toResumoResponse(Lembrete lembrete) {
        if (lembrete == null) return null;

        LembreteResumoResponse response = new LembreteResumoResponse();
        response.setId(lembrete.getId());
        response.setTitulo(lembrete.getTitulo());
        response.setDescricao(lembrete.getDescricao());
        response.setData(lembrete.getData());
        response.setPrioridade(lembrete.getPrioridade());
        response.setCategoria(lembrete.getCategoria());
        response.setConcluido(lembrete.getConcluido());

        return response;
    }
}
