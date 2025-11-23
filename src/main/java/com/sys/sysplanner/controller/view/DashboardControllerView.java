package com.sys.sysplanner.controller.view;

import com.sys.sysplanner.DTO.request.LembreteCreateRequest;
import com.sys.sysplanner.DTO.request.LembreteFormRequest;
import com.sys.sysplanner.DTO.request.LembreteUpdateRequest;
import com.sys.sysplanner.DTO.response.LembreteResponse;
import com.sys.sysplanner.domain.Lembrete;
import com.sys.sysplanner.domain.Usuario;
import com.sys.sysplanner.domain.enums.Prioridade;
import com.sys.sysplanner.repository.UsuarioRepository;
import com.sys.sysplanner.repository.LembreteRepository;
import com.sys.sysplanner.service.LembreteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class    DashboardControllerView {

    private final LembreteRepository lembreteRepository;
    private final UsuarioRepository usuarioRepository;
    private final LembreteService lembreteService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        model.addAttribute("lembretes", lembreteRepository.findByUsuario(usuario));
        model.addAttribute("nomeUsuario", usuario.getNome());
        return "dashboard";
    }

    @PostMapping("/lembretes/delete/{id}")
    public String deleteLembrete(@PathVariable Long id, Authentication authentication) {
        // Deleta o lembrete
        lembreteService.delete(id);

        // Redireciona para dashboard atualizado
        return "redirect:/dashboard";
    }

    @GetMapping("/lembretes/create")
    public String showCreateForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        // Criamos um DTO vazio para o form
        LembreteCreateRequest dto = new LembreteCreateRequest();
        dto.setUsuarioId(usuario.getId());

        model.addAttribute("lembrete", dto);
        return "lembrete-create";
    }

    @PostMapping("/lembretes/create")
    public String createLembrete(@ModelAttribute("lembrete") LembreteCreateRequest form) {
        lembreteService.create(form); // usa o service para salvar
        return "redirect:/dashboard"; // volta para a dashboard
    }

    @GetMapping("/lembretes/edit/{id}")
    public String editLembreteForm(@PathVariable Long id, Model model) {
        // Busca o lembrete como Response
        LembreteResponse lembreteResp = lembreteService.findById(id);

        // Cria DTO de edição
        LembreteUpdateRequest dto = new LembreteUpdateRequest();
        dto.setId(lembreteResp.getId());
        dto.setTitulo(lembreteResp.getTitulo());
        dto.setDescricao(lembreteResp.getDescricao());
        dto.setData(lembreteResp.getData());
        dto.setPrioridade(lembreteResp.getPrioridade());
        dto.setCategoria(lembreteResp.getCategoria());
        dto.setUsuarioId(lembreteResp.getUsuario().getId());

        model.addAttribute("lembrete", dto);
        return "lembrete-edit";
    }

    @PostMapping("/lembretes/update/{id}")
    public String updateLembrete(@PathVariable Long id, @ModelAttribute LembreteUpdateRequest dto) {
        lembreteService.update(id, dto);
        return "redirect:/dashboard";
    }
}
