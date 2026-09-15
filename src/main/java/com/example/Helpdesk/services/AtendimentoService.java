package com.example.Helpdesk.services;

import com.example.Helpdesk.dto.AtendimentoRequestDto;
import com.example.Helpdesk.dto.AtendimentoResponseDto;
import com.example.Helpdesk.dto.AtendimentoUpdateRequestDto;
import com.example.Helpdesk.model.AtendimentoModel;
import com.example.Helpdesk.model.ChamadoModel;
import com.example.Helpdesk.model.ChamadosEnum.NivelSuporte;
import com.example.Helpdesk.model.ChamadosEnum.PerfilUsuario;
import com.example.Helpdesk.model.ChamadosEnum.Prioridade;
import com.example.Helpdesk.model.ChamadosEnum.StatusChamado;
import com.example.Helpdesk.model.UsuarioModel;
import com.example.Helpdesk.repository.AtendimentoRepository;
import com.example.Helpdesk.repository.ChamadoRepository;
import com.example.Helpdesk.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço principal para registros de atendimento.
 * Gerencia mensagens de chat, atualizações de status e persistência do histórico do chamado.
 */
@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                              ChamadoRepository chamadoRepository,
                              UsuarioRepository usuarioRepository,
                              @Autowired(required = false) SimpMessagingTemplate messagingTemplate) {
        this.atendimentoRepository = atendimentoRepository;
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public AtendimentoResponseDto registrarAtendimento(AtendimentoRequestDto dto) {
        ChamadoModel chamado = chamadoRepository.findById(dto.getChamadoId())
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado com o ID: " + dto.getChamadoId()));

        // Garante que o usuarioId do remetente seja informado na requisição
        if (dto.getUsuarioId() == null) {
            throw new RuntimeException("O ID do usuário remetente (usuarioId) é obrigatório.");
        }

        UsuarioModel remetente = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário remetente não encontrado com o ID: " + dto.getUsuarioId()));

        AtendimentoModel atendimento = new AtendimentoModel();
        atendimento.setChamado(chamado);
        atendimento.setUsuario(remetente); // Define o autor real da mensagem
        atendimento.setObservacao(dto.getObservacao());

        // Se for um atendimento técnico/mudança de status
        if (dto.getTecnicoId() != null && remetente.getPerfil() != PerfilUsuario.CLIENTE) {
            UsuarioModel tecnico = usuarioRepository.findById(dto.getTecnicoId())
                    .orElseThrow(() -> new RuntimeException("Técnico não encontrado com o ID: " + dto.getTecnicoId()));

            if (dto.getNivelSuporte() != null) {
                validarPermissoes(tecnico, chamado, dto.getNivelSuporte());
            }

            atendimento.setTecnico(tecnico);

            if (dto.getPrioridade() != null) chamado.setPrioridade(dto.getPrioridade());
            if (dto.getStatus() != null) chamado.setStatus(dto.getStatus());
            if (dto.getNivelSuporte() != null) chamado.setNivelAtual(dto.getNivelSuporte());

            chamado.setTecnicoAtribuido(tecnico);
            chamadoRepository.save(chamado);

            // CORREÇÃO: o registro de atendimento (AtendimentoModel) nunca estava
            // recebendo prioridade/status/nivelSuporte neste ramo — só o ChamadoModel
            // era atualizado. Isso deixava esses campos nulos no histórico do
            // atendimento e, como a coluna nivel_suporte no banco é NOT NULL (o
            // ddl-auto=update do Hibernate não relaxa constraints em colunas já
            // existentes), o INSERT falhava com "Column 'nivel_suporte' cannot be
            // null" (erro 409 exibido no front-end).
            // Aqui gravamos no atendimento o valor que acabou de ser aplicado ao
            // chamado (ou o valor anterior do chamado, se nada foi alterado nesta
            // requisição), sempre com um fallback seguro para nunca persistir nulo.
            atendimento.setPrioridade(resolverPrioridade(dto.getPrioridade(), chamado.getPrioridade()));
            atendimento.setStatus(resolverStatus(dto.getStatus(), chamado.getStatus()));
            atendimento.setNivelSuporte(resolverNivelSuporte(dto.getNivelSuporte(), chamado.getNivelAtual()));
        } else {
            // Mensagem simples de chat (sem alteração formal de status/técnico).
            // CORREÇÃO: antes o nivelSuporte era lido só de chamado.getNivelAtual(),
            // que pode ser nulo em chamados antigos criados antes desse campo
            // existir/ter valor padrão. Como a coluna é NOT NULL no banco, qualquer
            // mensagem de chat num chamado "legado" derrubava o insert com 409.
            // Agora priorizamos o valor enviado pelo front-end (dto.getNivelSuporte())
            // e, na ausência dele, caímos para o valor do chamado; se nem esse
            // existir, usamos N1 como padrão seguro — o insert nunca mais recebe null.
            atendimento.setTecnico(chamado.getTecnicoAtribuido());
            atendimento.setPrioridade(resolverPrioridade(dto.getPrioridade(), chamado.getPrioridade()));
            atendimento.setStatus(resolverStatus(dto.getStatus(), chamado.getStatus()));
            atendimento.setNivelSuporte(resolverNivelSuporte(dto.getNivelSuporte(), chamado.getNivelAtual()));
        }

        AtendimentoModel salvo = atendimentoRepository.save(atendimento);
        AtendimentoResponseDto responseDto = converterParaDto(salvo);

        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/chamado/" + chamado.getId(), responseDto);
        }

        return responseDto;
    }
    public List<AtendimentoResponseDto> listarPorChamado(Long chamadoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isTecnicoOuAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TECNICO") || a.getAuthority().equals("ROLE_ADMIN"));

        return atendimentoRepository.findByChamadoId(chamadoId).stream()
                .map(atendimento -> isTecnicoOuAdmin ? new AtendimentoResponseDto(atendimento) : AtendimentoResponseDto.paraCliente(atendimento))
                .collect(Collectors.toList());
    }

    public List<AtendimentoResponseDto> listarTodos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isTecnicoOuAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TECNICO") || a.getAuthority().equals("ROLE_ADMIN"));

        List<AtendimentoModel> atendimentos = atendimentoRepository.findAll();

        return atendimentos.stream()
                .map(atendimento -> isTecnicoOuAdmin ? new AtendimentoResponseDto(atendimento) : AtendimentoResponseDto.paraCliente(atendimento))
                .toList();
    }

    public void excluir(Long id) {
        if (!atendimentoRepository.existsById(id)) {
            throw new RuntimeException("Atendimento não encontrado com o ID: " + id);
        }
        atendimentoRepository.deleteById(id);
    }

    public AtendimentoResponseDto atualizar(Long id, AtendimentoUpdateRequestDto dto) {
        AtendimentoModel atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado com o ID: " + id));

        if (dto.observacao() != null && !dto.observacao().isBlank()) {
            atendimento.setObservacao(dto.observacao());
        }
        if (dto.prioridade() != null) {
            atendimento.setPrioridade(dto.prioridade());
        }
        if (dto.status() != null) {
            atendimento.setStatus(dto.status());
        }
        if (dto.nivelSuporte() != null) {
            atendimento.setNivelSuporte(dto.nivelSuporte());
        }

        AtendimentoModel atualizado = atendimentoRepository.save(atendimento);
        return converterParaDto(atualizado);
    }

    // ----------------------------------------------------------------------------
    // Métodos de apoio: garantem que prioridade/status/nivelSuporte do atendimento
    // NUNCA sejam nulos antes do save(), independente do estado do chamado
    // associado (que pode ter sido criado antes desses campos existirem/terem
    // valor padrão). Isso evita a violação de constraint NOT NULL no banco
    // (coluna nivel_suporte) que gerava o erro 409 visto no front-end.
    // ----------------------------------------------------------------------------

    private Prioridade resolverPrioridade(Prioridade doDto, Prioridade doChamado) {
        if (doDto != null) return doDto;
        if (doChamado != null) return doChamado;
        return Prioridade.BAIXA; // valor padrão seguro
    }

    private StatusChamado resolverStatus(StatusChamado doDto, StatusChamado doChamado) {
        if (doDto != null) return doDto;
        if (doChamado != null) return doChamado;
        return StatusChamado.ABERTO; // valor padrão seguro
    }

    private NivelSuporte resolverNivelSuporte(NivelSuporte doDto, NivelSuporte doChamado) {
        if (doDto != null) return doDto;
        if (doChamado != null) return doChamado;
        return NivelSuporte.N1; // valor padrão seguro (evita erro 409 em chamados legados sem nível definido)
    }

    private void validarPermissoes(UsuarioModel tecnico, ChamadoModel chamado, NivelSuporte novoNivel) {
        if (tecnico.getPerfil() == PerfilUsuario.ADMIN) {
            return;
        }
        if (tecnico.getPerfil() == PerfilUsuario.CLIENTE) {
            throw new RuntimeException("Permissão negada: Você não tem autorização para atender chamados");
        }
        if (tecnico.getPerfil() == PerfilUsuario.TECNICO_N1 && chamado.getNivelAtual() != NivelSuporte.N1) {
            throw new RuntimeException("Permissão negada: Técnico N1 não tem autorização para alterar chamados de nível " + chamado.getNivelAtual());
        }
        if (tecnico.getPerfil() == PerfilUsuario.TECNICO_N2 && chamado.getNivelAtual() == NivelSuporte.N3) {
            throw new RuntimeException("Permissão negada: Técnico N2 não tem acesso a chamados do N3");
        }
        if (tecnico.getPerfil() == PerfilUsuario.TECNICO_N1 && novoNivel == NivelSuporte.N3) {
            throw new RuntimeException("Permissão negada: Técnico N1 não pode transferir chamados diretamente para o N3");
        }
    }

    private AtendimentoResponseDto converterParaDto(AtendimentoModel atendimento) {
        return new AtendimentoResponseDto(atendimento);
    }
}