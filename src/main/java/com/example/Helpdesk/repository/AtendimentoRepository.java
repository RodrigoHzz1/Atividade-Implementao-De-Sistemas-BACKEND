package com.example.Helpdesk.repository;

import com.example.Helpdesk.model.AtendimentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<AtendimentoModel, Long> {
    List<AtendimentoModel> findByChamadoId(Long chamadoId);
<<<<<<< HEAD
    List<AtendimentoModel> findByTecnicoId(Long tecnicoId);
=======
>>>>>>> 2e66d3441d0026350e888c13eaacd8e148c1c33e
}