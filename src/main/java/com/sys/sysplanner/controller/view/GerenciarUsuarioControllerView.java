package com.sys.sysplanner.controller.view;

import com.sys.sysplanner.DTO.request.UsuarioAdminUpdateRequest;
import com.sys.sysplanner.DTO.request.UsuarioFilterRequest;
import com.sys.sysplanner.DTO.response.UsuarioResponse;
import com.sys.sysplanner.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gerenciar")
public class GerenciarUsuarioControllerView {

    private final UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public String listarUsuarios(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String cidade,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        UsuarioFilterRequest filtro = new UsuarioFilterRequest();
        filtro.setNome(nome);
        filtro.setEmail(email);
        filtro.setCpf(cpf);
        filtro.setEstado(estado);
        filtro.setCidade(cidade);

        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());

        Page<UsuarioResponse> usuariosPage = usuarioService.findAllFiltered(filtro, pageable);

        model.addAttribute("usuarios", usuariosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usuariosPage.getTotalPages());
        model.addAttribute("filtro", filtro);

        return "gerenciar/usuarios";
    }

    // EXCLUSÃO FINAL
    @GetMapping("/usuario/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        usuarioService.delete(id);
        return "redirect:/gerenciar/usuarios?excluido";
    }

    // EDITAR (FORM)
    @GetMapping("/usuario/editar/{id}")
    public String formEditarUsuario(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.findById(id));
        model.addAttribute("dto", new UsuarioAdminUpdateRequest());
        return "gerenciar/editar_usuario";
    }

    // SALVAR ALTERAÇÕES
    @PostMapping("/usuario/editar/{id}")
    public String atualizarUsuario(@PathVariable Long id,
                                   @ModelAttribute("dto") UsuarioAdminUpdateRequest dto,
                                   Model model) {
        try {
            usuarioService.updateByAdmin(id, dto);
            return "redirect:/gerenciar/usuarios?atualizado";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuarioService.findById(id));
            return "gerenciar/editar_usuario";
        }
    }
}