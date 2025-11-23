package com.sys.sysplanner.controller;

import com.sys.sysplanner.DTO.request.LembreteCreateRequest;
import com.sys.sysplanner.DTO.request.LembreteUpdateRequest;
import com.sys.sysplanner.DTO.response.LembreteResponse;
import com.sys.sysplanner.DTO.response.LembreteResumoResponse;
import com.sys.sysplanner.service.LembreteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lembretes")
@RequiredArgsConstructor
public class LembreteController {

    private final LembreteService lembreteService;

    // CREATE
    @PostMapping
    public ResponseEntity<LembreteResponse> create(
            @Valid @RequestBody LembreteCreateRequest dto) {
        LembreteResponse response = lembreteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // READ by ID do lembrete (com usuário)
    @GetMapping("/{id}")
    public ResponseEntity<LembreteResponse> findById(@PathVariable Long id) {
        LembreteResponse response = lembreteService.findById(id);
        return ResponseEntity.ok(response);
    }

    // READ by ID do usuário (resumo, sem usuário)
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<LembreteResumoResponse>> findByUsuario(@PathVariable Long usuarioId) {
        List<LembreteResumoResponse> response = lembreteService.findByUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<LembreteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LembreteUpdateRequest dto) {
        LembreteResponse updated = lembreteService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        lembreteService.delete(id);
    }
}
