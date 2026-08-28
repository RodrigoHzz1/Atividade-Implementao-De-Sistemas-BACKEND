package com.example.Helpdesk.services;
import com.example.Helpdesk.dto.UsuarioResponseDto;
import com.example.Helpdesk.dto.UsuarioResquestDto;
import com.example.Helpdesk.model.UsuarioModel;
import com.example.Helpdesk.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.Helpdesk.model.ChamadosEnum.PerfilUsuario;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDto criar(UsuarioResquestDto dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado no sistema.");
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());


        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        UsuarioModel usuarioSalvo = usuarioRepository.save(usuario);
        return converterParaDto(usuarioSalvo);
    }

    public UsuarioResponseDto atualizar(Long id, UsuarioResquestDto dto) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        UsuarioModel usuarioAtualizado = usuarioRepository.save(usuario);
        return converterParaDto(usuarioAtualizado);
    }

    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDto buscarPorId(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
        return converterParaDto(usuario);
    }

    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponseDto converterParaDto(UsuarioModel usuario) {
        return new UsuarioResponseDto(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public UsuarioResponseDto alterarPerfil(Long id, PerfilUsuario novoPerfil) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        usuario.setPerfil(novoPerfil);

        UsuarioModel usuarioAtualizado = usuarioRepository.save(usuario);
        return converterParaDto(usuarioAtualizado);
    }


    public UsuarioResponseDto promoverParaAdmin(Long id) {
        return alterarPerfil(id, PerfilUsuario.ADMIN);
    }
}