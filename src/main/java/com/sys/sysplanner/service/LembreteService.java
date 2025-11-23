package com.sys.sysplanner.service;

import com.sys.sysplanner.DTO.request.LembreteCreateRequest;
import com.sys.sysplanner.DTO.request.LembreteUpdateRequest;
import com.sys.sysplanner.DTO.response.LembreteResponse;
import com.sys.sysplanner.domain.Lembrete;
import com.sys.sysplanner.domain.Usuario;
import com.sys.sysplanner.mapper.LembreteMapper;
import com.sys.sysplanner.repository.LembreteRepository;
import com.sys.sysplanner.repository.UsuarioRepository;
import com.sys.sysplanner.DTO.response.LembreteResumoResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LembreteService {

    private final LembreteRepository lembreteRepository;
    private final UsuarioRepository usuarioRepository;
    private final LembreteMapper lembreteMapper;

    // CREATE
    @CacheEvict(value = "lembretesPorUsuario", key = "#dto.usuarioId")
    public LembreteResponse create(LembreteCreateRequest dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Lembrete lembrete = lembreteMapper.toEntity(dto, usuario);
        Lembrete saved = lembreteRepository.save(lembrete);
        return lembreteMapper.toResponse(saved);
    }

    // READ by ID
    public LembreteResponse findById(Long id) {
        Lembrete lembrete = lembreteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lembrete não encontrado"));

        return lembreteMapper.toResponse(lembrete);
    }

    // READ all by user
    @Cacheable(value = "lembretesPorUsuario", key = "#usuarioId")
    public List<LembreteResumoResponse> findByUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        return lembreteRepository.findByUsuario(usuario).stream()
                .map(lembreteMapper::toResumoResponse) // usa o mapper sem o usuário
                .collect(Collectors.toList());
    }

    // UPDATE
    @CacheEvict(value = "lembretesPorUsuario", allEntries = true)
    public LembreteResponse update(Long id, LembreteUpdateRequest dto) {
        Lembrete lembrete = lembreteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lembrete não encontrado"));

        lembrete.setTitulo(dto.getTitulo());
        lembrete.setDescricao(dto.getDescricao());
        lembrete.setData(dto.getData());
        lembrete.setPrioridade(dto.getPrioridade());
        lembrete.setCategoria(dto.getCategoria());
        lembrete.setConcluido(dto.getConcluido());

        Lembrete updated = lembreteRepository.save(lembrete);
        return lembreteMapper.toResponse(updated);
    }


    // DELETE
    @CacheEvict(value = "lembretesPorUsuario", allEntries = true)
    public void delete(Long id) {
        if (!lembreteRepository.existsById(id)) {
            throw new EntityNotFoundException("Lembrete não encontrado");
        }
        lembreteRepository.deleteById(id);
    }
}
