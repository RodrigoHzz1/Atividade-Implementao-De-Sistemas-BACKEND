package com.example.Helpdesk.repository;

import com.example.Helpdesk.model.AtendimentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo histórico de atendimentos.
 */
@Repository
public interface AtendimentoRepository extends JpaRepository<AtendimentoModel, Long> {
    List<AtendimentoModel> findByChamadoId(Long chamadoId);
    List<AtendimentoModel> findByTecnicoId(Long tecnicoId);
}