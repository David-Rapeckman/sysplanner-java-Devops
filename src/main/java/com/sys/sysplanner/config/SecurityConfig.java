package com.sys.sysplanner.config;

import com.sys.sysplanner.domain.enums.Role;
import com.sys.sysplanner.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Páginas públicas
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**")
                        .permitAll()

                        // -------------------------------
                        // PÁGINAS ADMIN
                        // -------------------------------
                        .requestMatchers("/gerenciar/usuarios/**", "admin/**")

                        .hasRole("ADMIN")

                        // -------------------------------
                        // PÁGINAS DO USUÁRIO LOGADO
                        // USER + ADMIN
                        // -------------------------------
                        .requestMatchers(
                                "/dashboard",
                                "/usuario/perfil",
                                "/usuario/editar",
                                "/usuario/excluir"
                        ).hasAnyRole("USER", "ADMIN")

                        // Qualquer outro endpoint precisa estar logado
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                );

        return http.build();
    }
}
