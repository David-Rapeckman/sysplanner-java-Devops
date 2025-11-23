package com.sys.sysplanner.specifications;

import com.sys.sysplanner.domain.Usuario;
import com.sys.sysplanner.DTO.request.UsuarioFilterRequest;
import com.sys.sysplanner.domain.enums.Estado;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecifications {

    public static Specification<Usuario> filtrar(UsuarioFilterRequest filtro) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                predicates = cb.and(predicates,
                        cb.like(cb.lower(root.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
            }

            if (filtro.getEmail() != null && !filtro.getEmail().isEmpty()) {
                predicates = cb.and(predicates,
                        cb.like(cb.lower(root.get("email")), "%" + filtro.getEmail().toLowerCase() + "%"));
            }

            if (filtro.getCpf() != null && !filtro.getCpf().isEmpty()) {
                predicates = cb.and(predicates,
                        cb.like(root.get("cpf"), "%" + filtro.getCpf() + "%"));
            }

            if (filtro.getEstado() != null && !filtro.getEstado().isEmpty()) {
                try {
                    Estado estadoEnum = Estado.valueOf(filtro.getEstado().toUpperCase());
                    predicates = cb.and(predicates,
                            cb.equal(root.get("endereco").get("estado"), estadoEnum));
                } catch (IllegalArgumentException e) {
                    // caso o valor enviado não seja um estado válido, ignora o filtro
                }
            }

            if (filtro.getCidade() != null && !filtro.getCidade().isEmpty()) {
                predicates = cb.and(predicates,
                        cb.like(cb.lower(root.get("endereco").get("cidade")), "%" + filtro.getCidade().toLowerCase() + "%"));
            }

            return predicates;
        };
    }
}
