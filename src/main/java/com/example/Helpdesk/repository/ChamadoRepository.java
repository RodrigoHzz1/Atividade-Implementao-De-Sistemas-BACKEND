package com.example.Helpdesk.repository;

import com.example.Helpdesk.model.ChamadoModel;
import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChamadoRepository extends JpaRepository<ChamadoModel, Long> {

    List<ChamadoModel> findBySolicitanteId(Long usuarioId);
<<<<<<< HEAD
    List<ChamadoModel> findByTecnicoAtribuidoId(Long tecnicoId);
=======
>>>>>>> 2e66d3441d0026350e888c13eaacd8e148c1c33e
    List<ChamadoModel> findByNivelAtual(NivelSuporte nivelAtual);
    List<ChamadoModel> findByNivelAtualIn(List<NivelSuporte> niveis);
}
