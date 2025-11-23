package com.sys.sysplanner.repository;

import com.sys.sysplanner.domain.Lembrete;
import com.sys.sysplanner.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LembreteRepository extends JpaRepository<Lembrete, Long> {

    List<Lembrete> findByUsuario(Usuario usuario);

}
