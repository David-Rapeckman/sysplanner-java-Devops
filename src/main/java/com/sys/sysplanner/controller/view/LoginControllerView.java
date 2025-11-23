package com.sys.sysplanner.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginControllerView {

    // Página de login
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Thymeleaf: src/main/resources/templates/login.html
    }
}
