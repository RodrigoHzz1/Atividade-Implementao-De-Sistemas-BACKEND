package com.example.Helpdesk.controller;

import com.example.Helpdesk.dto.RespostaApiDto;
import com.example.Helpdesk.dto.UsuarioResponseDto;
import com.example.Helpdesk.dto.UsuarioResquestDto;
import com.example.Helpdesk.model.ChamadosEnum.PerfilUsuario;
import com.example.Helpdesk.model.UsuarioModel;
import com.example.Helpdesk.repository.UsuarioRepository;
import com.example.Helpdesk.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para gestão de usuários.
 * Expõe endpoints de cadastro, listagem, atualização, alteração de perfil e exclusão.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    // Construtor atualizado com a injeção do UsuarioService
    public UsuarioController(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder,
                             UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<RespostaApiDto<UsuarioModel>> cadastrar(@Valid @RequestBody UsuarioModel usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        UsuarioModel salvo = usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RespostaApiDto<>("Usuário cadastrado com sucesso!", salvo));
    }

    @GetMapping
    public ResponseEntity<RespostaApiDto<List<UsuarioResponseDto>>> listarTodos() {
        List<UsuarioResponseDto> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(new RespostaApiDto<>("Usuários listados com sucesso!", usuarios));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespostaApiDto<UsuarioResponseDto>> atualizar(
            @PathVariable Long id, @RequestBody UsuarioResquestDto dto) {
        // Sem @Valid de propósito: editar nome/e-mail não deve exigir
        // reenviar a senha (o service só troca a senha se ela vier preenchida).
        UsuarioResponseDto atualizado = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(new RespostaApiDto<>("Usuário atualizado com sucesso!", atualizado));
    }

    @PatchMapping("/{id}/perfil")
    public ResponseEntity<RespostaApiDto<UsuarioResponseDto>> alterarPerfil(
            @PathVariable Long id,
            @RequestParam String perfil) {

        PerfilUsuario perfilEnum = PerfilUsuario.valueOf(perfil.trim().toUpperCase());

        UsuarioResponseDto usuarioAtualizado = usuarioService.alterarPerfil(id, perfilEnum);

        return ResponseEntity.ok(
                new RespostaApiDto<>("Perfil alterado com sucesso!", usuarioAtualizado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespostaApiDto<Void>> excluir(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.ok(new RespostaApiDto<>("Usuário excluído com sucesso!"));
    }
}