package com.example.Helpdesk.repository;

import com.example.Helpdesk.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório de acesso aos usuários.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    Optional<UsuarioModel> findByEmail (String email);

    boolean existsByEmail (String email);
}
