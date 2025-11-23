package com.sys.sysplanner.controller.view;

import com.sys.sysplanner.DTO.request.UsuarioCreateRequest;
import com.sys.sysplanner.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class RegisterControllerView {

    private final UsuarioService usuarioService;

    // Exibe o formulário de registro
    @GetMapping("/register")
    public String exibirFormulario(Model model) {
        model.addAttribute("dto", new UsuarioCreateRequest());
        return "register";
    }

    // Recebe dados do formulário
    @PostMapping("/register")
    public String registrarUsuario(@Valid @ModelAttribute("dto") UsuarioCreateRequest dto,
                                   BindingResult result,
                                   Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        try {
            usuarioService.create(dto);
            return "redirect:/login?registrado"; // Redireciona para login com mensagem
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "register";
        }
    }
}