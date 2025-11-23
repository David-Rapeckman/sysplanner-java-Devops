package com.sys.sysplanner.service;

import com.sys.sysplanner.DTO.response.UsuarioResponse;
import com.sys.sysplanner.domain.Usuario;
import com.sys.sysplanner.domain.enums.Role;
import com.sys.sysplanner.repository.LembreteRepository;
import com.sys.sysplanner.repository.UsuarioRepository;
import com.sys.sysplanner.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardAdmService {

    private final UsuarioRepository usuarioRepository;
    private final LembreteRepository lembreteRepository;
    private final UsuarioMapper usuarioMapper;

    // ========================
    // RESUMO GERAL
    // ========================

    // Total de usuários
    public long countUsuarios() {
        return usuarioRepository.count();
    }

    // Últimos usuários cadastrados (padrão: 5)
    public List<UsuarioResponse> ultimosUsuarios(int quantidade) {
        return usuarioRepository.findAll(PageRequest.of(0, quantidade, Sort.by("id").descending()))
                .stream()
                .map(usuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Média de lembretes por usuário
    public double mediaLembretesPorUsuario() {
        long totalUsuarios = usuarioRepository.count();
        long totalLembretes = lembreteRepository.count();
        return totalUsuarios == 0 ? 0 : (double) totalLembretes / totalUsuarios;
    }

    // ========================
    // GRÁFICOS
    // ========================

    // Distribuição de roles
    public Map<String, Long> distribuicaoRoles() {
        Map<String, Long> mapa = new LinkedHashMap<>();
        usuarioRepository.findAll()
                .forEach(u -> mapa.merge(u.getRole().name(), 1L, Long::sum));
        return mapa;
    }

    // Distribuição de categorias dos lembretes
    public Map<String, Long> distribuicaoCategorias() {
        Map<String, Long> mapa = new LinkedHashMap<>();
        lembreteRepository.findAll()
                .forEach(l -> mapa.merge(l.getCategoria().name(), 1L, Long::sum));
        return mapa;
    }
}
