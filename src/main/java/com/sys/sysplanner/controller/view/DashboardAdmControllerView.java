package com.sys.sysplanner.controller.view;

import com.sys.sysplanner.service.DashboardAdmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
public class DashboardAdmControllerView {

    private final DashboardAdmService dashboardAdmService;

    @GetMapping
    public String dashboard(Model model) {
        // Resumo geral
        model.addAttribute("totalUsuarios", dashboardAdmService.countUsuarios());
        model.addAttribute("ultimosUsuarios", dashboardAdmService.ultimosUsuarios(5));
        model.addAttribute("mediaLembretes", dashboardAdmService.mediaLembretesPorUsuario());

        // Gráficos
        model.addAttribute("rolesDistribuicao", dashboardAdmService.distribuicaoRoles());
        model.addAttribute("categoriasDistribuicao", dashboardAdmService.distribuicaoCategorias());

        return "admin/dashboard";
    }
}
