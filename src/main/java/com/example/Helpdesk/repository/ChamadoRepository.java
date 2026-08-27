package com.example.Helpdesk.repository;

import com.example.Helpdesk.model.ChamadoModel;
import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChamadoRepository extends JpaRepository<ChamadoModel, Long> {

    List<ChamadoModel> findBySolicitanteId(Long usuarioId);
    List<ChamadoModel> findByNivelAtual(NivelSuporte nivelAtual);
    List<ChamadoModel> findByNivelAtualIn(List<NivelSuporte> niveis);
}
