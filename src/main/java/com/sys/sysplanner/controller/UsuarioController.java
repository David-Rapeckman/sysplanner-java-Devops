package com.sys.sysplanner.controller;

import com.sys.sysplanner.DTO.request.UsuarioCreateRequest;
import com.sys.sysplanner.DTO.request.UsuarioUpdateRequest;
import com.sys.sysplanner.DTO.response.UsuarioResponse;
import com.sys.sysplanner.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // CREATE
    @PostMapping
    public ResponseEntity<UsuarioResponse> create(
            @Valid @RequestBody UsuarioCreateRequest dto) {

        UsuarioResponse response = usuarioService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    // READ paginado
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findAll(pageable));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest dto) {

        UsuarioResponse updated = usuarioService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        usuarioService.delete(id);
    }
}
