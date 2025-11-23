package com.sys.sysplanner.mapper;

import com.sys.sysplanner.DTO.request.EnderecoRequest;
import com.sys.sysplanner.DTO.response.EnderecoResponse;
import com.sys.sysplanner.domain.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapper {

    public Endereco toEntity(EnderecoRequest dto) {
        if (dto == null) return null;

        Endereco endereco = new Endereco();
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());

        return endereco;
    }

    public EnderecoResponse toResponse(Endereco entity) {
        if (entity == null) return null;

        EnderecoResponse response = new EnderecoResponse();
        response.setRua(entity.getRua());
        response.setNumero(entity.getNumero());
        response.setComplemento(entity.getComplemento());
        response.setBairro(entity.getBairro());
        response.setCidade(entity.getCidade());
        response.setEstado(entity.getEstado());
        response.setCep(entity.getCep());

        return response;
    }

    public EnderecoRequest toRequest(EnderecoResponse dto) {
        if (dto == null) return null;

        EnderecoRequest req = new EnderecoRequest();
        req.setRua(dto.getRua());
        req.setNumero(dto.getNumero());
        req.setComplemento(dto.getComplemento());
        req.setBairro(dto.getBairro());
        req.setCidade(dto.getCidade());
        req.setEstado(dto.getEstado());
        req.setCep(dto.getCep());
        return req;
    }
}
