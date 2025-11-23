package com.sys.sysplanner.controller.view;

import com.sys.sysplanner.DTO.request.UsuarioUpdateRequest;
import com.sys.sysplanner.DTO.response.UsuarioResponse;
import com.sys.sysplanner.mapper.EnderecoMapper;
import com.sys.sysplanner.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioControllerView {

    private final UsuarioService usuarioService;
    private final EnderecoMapper enderecoMapper;

    // Página de perfil
    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {
        String email = auth.getName();
        UsuarioResponse usuario = usuarioService.findByEmail(email);

        model.addAttribute("usuario", usuario);
        return "usuario/perfil";
    }

    // Página de edição
    @GetMapping("/editar")
    public String editar(Model model, Authentication auth) {
        String email = auth.getName();
        UsuarioResponse usuario = usuarioService.findByEmail(email);

        UsuarioUpdateRequest dto = new UsuarioUpdateRequest();
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setEndereco(enderecoMapper.toRequest(usuario.getEndereco()));
        model.addAttribute("usuario", usuario);
        model.addAttribute("dto", dto);
        return "usuario/editar";
    }

    @PostMapping("/editar")
    public String atualizar(Authentication auth,
                            @ModelAttribute("dto") UsuarioUpdateRequest dto,
                            Model model) {

        String emailAtual = auth.getName();

        try {
            boolean emailFoiAlterado = usuarioService.updateAndCheckEmailChange(emailAtual, dto);

            if (emailFoiAlterado) {
                return "redirect:/logout?emailAlterado";
            }

            return "redirect:/usuario/perfil";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dto", dto);
            return "usuario/editar";
        }
    }

    // Excluir o próprio usuário
    @PostMapping("/excluir")
    public String excluir(Authentication auth) {

        String email = auth.getName();
        UsuarioResponse usuario = usuarioService.findByEmail(email);

        usuarioService.delete(usuario.getId());

        return "redirect:/logout";
    }
}
