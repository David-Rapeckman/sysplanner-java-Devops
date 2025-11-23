package com.sys.sysplanner.config;

import com.sys.sysplanner.domain.Endereco;
import com.sys.sysplanner.domain.Lembrete;
import com.sys.sysplanner.domain.Usuario;
import com.sys.sysplanner.domain.enums.Categoria;
import com.sys.sysplanner.domain.enums.Estado;
import com.sys.sysplanner.domain.enums.Prioridade;
import com.sys.sysplanner.domain.enums.Role;
import com.sys.sysplanner.repository.LembreteRepository;
import com.sys.sysplanner.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final LembreteRepository lembreteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() > 0) {
            return; // evita criar duplicados se a aplicação reiniciar
        }

        List<Usuario> usuarios = new ArrayList<>();

        // Criando 10 usuários
        for (int i = 1; i <= 10; i++) {
            Usuario usuario = new Usuario();
            usuario.setNome("Usuario " + i);
            usuario.setEmail("usuario" + i + "@email.com");

            // CPF apenas números, garantindo que seja único
            usuario.setCpf(String.format("000000000%02d", i)); // ex: 00000000001, 00000000002...

            usuario.setSenha(passwordEncoder.encode("senha" + i));

            // Role: apenas o primeiro usuário será ADMIN
            usuario.setRole(i == 1 ? Role.ADMIN : Role.USER);

            // Criando endereço embutido sem builder
            Endereco endereco = new Endereco();
            endereco.setRua("Rua " + i);
            endereco.setNumero(String.valueOf(100 + i));
            endereco.setComplemento("Apto " + i);
            endereco.setBairro("Bairro " + i);
            endereco.setCidade("Cidade " + i);
            endereco.setEstado(Estado.SP);
            endereco.setCep(String.format("00000-00%02d", i));
            usuario.setEndereco(endereco);
            usuarios.add(usuario);
        }

// Salvando todos os usuários com endereços embutidos
        usuarioRepository.saveAll(usuarios);


        // Criando 3 lembretes para cada usuário
        Random random = new Random();
        Categoria[] categorias = Categoria.values();
        Prioridade[] prioridades = Prioridade.values();

        for (Usuario usuario : usuarios) {
            List<Lembrete> lembretes = new ArrayList<>();
            for (int j = 1; j <= 3; j++) {

                // Escolhendo categoria e prioridade aleatoriamente
                Categoria categoria = categorias[random.nextInt(categorias.length)];
                Prioridade prioridade = prioridades[random.nextInt(prioridades.length)];

                // Gerando descrição com base na categoria
                String descricao = switch (categoria) {
                    case SAUDE -> "Cuidar da saúde é essencial, lembrete " + j;
                    case LAZER -> "Hora de se divertir! Lembrete " + j;
                    case FAMILIA -> "Tempo com a família é importante. Lembrete " + j;
                    case PROFISSIONAL -> "Tarefa profissional a realizar. Lembrete " + j;
                    case OUTRO -> "Outro lembrete genérico. Lembrete " + j;
                };

                Lembrete lembrete = new Lembrete();
                lembrete.setTitulo("Lembrete " + j + " de " + usuario.getNome());
                lembrete.setDescricao(descricao);
                lembrete.setData(LocalDateTime.now().plusDays(j));
                lembrete.setPrioridade(prioridade);
                lembrete.setCategoria(categoria);
                lembrete.setConcluido("N");
                lembrete.setUsuario(usuario);

                lembretes.add(lembrete);
            }
            lembreteRepository.saveAll(lembretes);
        }

        System.out.println("Database Seeder: Usuários e lembretes criados com sucesso!");
    }
}