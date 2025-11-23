package com.sys.sysplanner.service;

import com.sys.sysplanner.DTO.request.UsuarioCreateRequest;
import com.sys.sysplanner.DTO.request.UsuarioFilterRequest;
import com.sys.sysplanner.DTO.request.UsuarioUpdateRequest;
import com.sys.sysplanner.DTO.request.UsuarioAdminUpdateRequest;
import com.sys.sysplanner.DTO.response.UsuarioResponse;
import com.sys.sysplanner.config.RabbitMQConfig;
import com.sys.sysplanner.domain.Usuario;
import com.sys.sysplanner.domain.enums.*;
import com.sys.sysplanner.mapper.UsuarioMapper;
import com.sys.sysplanner.mq.EmailMessage;
import com.sys.sysplanner.repository.UsuarioRepository;
import com.sys.sysplanner.specifications.UsuarioSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    // GET ALL
    @Cacheable(value = "usuarios")
    public Page<UsuarioResponse> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::toResponse);
    }

    // CREATE
    @CacheEvict(value = "usuarios", allEntries = true)
    public UsuarioResponse create(UsuarioCreateRequest dto) {

        // Regra: email único
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("Email já cadastrado");
        }

        // Regra: CPF único
        if (usuarioRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new DataIntegrityViolationException("CPF já cadastrado");
        }

        Usuario usuario = usuarioMapper.toEntity(dto);

        // Forçar ROLE USER na criação
        usuario.setRole(Role.USER);

        // Criptografar senha
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        // Salvar no banco
        Usuario saved = usuarioRepository.save(usuario);

        // Enviar e-mail assíncrono via RabbitMQ
        EmailMessage emailMessage = new EmailMessage(
                saved.getEmail(),
                "Bem-vindo ao SysPlanner!",
                "<p>Olá " + saved.getNome() + ",</p>" +
                        "<p>Obrigado por se registrar no SysPlanner!</p>"
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_QUEUE, emailMessage);

        return usuarioMapper.toResponse(saved);
    }


    // GET BY ID
    public UsuarioResponse findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        return usuarioMapper.toResponse(usuario);
    }

    // GET BY EMAIL
    public UsuarioResponse findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return usuarioMapper.toResponse(usuario);
    }

    // UPDATE
    @CacheEvict(value = "usuarios", allEntries = true)
    public UsuarioResponse update(Long id, UsuarioUpdateRequest dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // Regra: email não pode duplicar
        usuarioRepository.findByEmail(dto.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new DataIntegrityViolationException("Email já está sendo usado por outro usuário");
                });

        // Mapper aplica alterações permitidas
        usuarioMapper.updateEntity(usuario, dto);

        // Atualizar senha apenas se enviada
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Usuario updated = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(updated);
    }

    public boolean updateAndCheckEmailChange(String emailAtual, UsuarioUpdateRequest dto) {

        Usuario usuario = usuarioRepository.findByEmail(emailAtual)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean emailFoiAlterado = dto.getEmail() != null &&
                !dto.getEmail().isBlank() &&
                !dto.getEmail().equalsIgnoreCase(usuario.getEmail());

        update(usuario.getId(), dto);

        return emailFoiAlterado;
    }

    @CacheEvict(value = "usuarios", allEntries = true)
    public void updateByAdmin(Long id, UsuarioAdminUpdateRequest dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Regra: email não pode duplicar
        usuarioRepository.findByEmail(dto.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new DataIntegrityViolationException("Email já está sendo usado por outro usuário");
                });

        // Nome e Email
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        usuario.getEndereco().setRua(dto.getRua());
        usuario.getEndereco().setNumero(dto.getNumero());
        usuario.getEndereco().setBairro(dto.getBairro());
        usuario.getEndereco().setComplemento(dto.getComplemento());
        usuario.getEndereco().setCidade(dto.getCidade());
        usuario.getEndereco().setEstado(dto.getEstado());
        usuario.getEndereco().setCep(dto.getCep());

        // Atualizar role
        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            usuario.setRole(Role.valueOf(dto.getRole()));
        }

        // Atualizar senha se enviada
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {

            if (!dto.getSenha().equals(dto.getConfirmarSenha())) {
                throw new RuntimeException("As senhas não são iguais.");
            }

            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        usuarioRepository.save(usuario);
    }


    // DELETE
    @CacheEvict(value = "usuarios", allEntries = true)
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    // GET ALL
    @Cacheable(value = "usuarios")
    public List<UsuarioResponse> findAllList() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    // GET FILTRADO COM PAGINAÇÃO
    @Cacheable(value = "usuarios")
    public Page<UsuarioResponse> findAllFiltered(UsuarioFilterRequest filtro, Pageable pageable) {
        Specification<Usuario> spec = UsuarioSpecifications.filtrar(filtro);
        return usuarioRepository.findAll(spec, pageable)
                .map(usuarioMapper::toResponse);
    }
}
