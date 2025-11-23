package com.sys.sysplanner.mapper;

import com.sys.sysplanner.DTO.request.UsuarioCreateRequest;
import com.sys.sysplanner.DTO.request.UsuarioUpdateRequest;
import com.sys.sysplanner.DTO.response.UsuarioResponse;
import com.sys.sysplanner.domain.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final EnderecoMapper enderecoMapper;

    // CREATE
    public Usuario toEntity(UsuarioCreateRequest dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setCpf(dto.getCpf());

        usuario.setEndereco(enderecoMapper.toEntity(dto.getEndereco()));

        return usuario;
    }

    public void updateEntity(Usuario usuario, UsuarioUpdateRequest dto) {
        if (usuario == null || dto == null) return;

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        usuario.setEndereco(enderecoMapper.toEntity(dto.getEndereco()));

    }

    // RESPONSE
    public UsuarioResponse toResponse(Usuario entity) {
        if (entity == null) return null;

        UsuarioResponse response = new UsuarioResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setEmail(entity.getEmail());
        response.setCpf(entity.getCpf());
        response.setRole(entity.getRole());

        // Endereço no response
        response.setEndereco(enderecoMapper.toResponse(entity.getEndereco()));

        return response;
    }
}
