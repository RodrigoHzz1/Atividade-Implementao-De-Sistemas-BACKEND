package com.example.Helpdesk.controller;

import com.example.Helpdesk.dto.RespostaApiDto;
import com.example.Helpdesk.dto.UsuarioResponseDto;
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

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

<<<<<<< HEAD
    // Construtor atualizado com a injeção do UsuarioService
=======
>>>>>>> 2e66d3441d0026350e888c13eaacd8e148c1c33e
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

<<<<<<< HEAD
    @GetMapping
    public ResponseEntity<RespostaApiDto<List<UsuarioResponseDto>>> listarTodos() {
        List<UsuarioResponseDto> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(new RespostaApiDto<>("Usuários listados com sucesso!", usuarios));
    }

=======
>>>>>>> 2e66d3441d0026350e888c13eaacd8e148c1c33e
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

<<<<<<< HEAD
    @DeleteMapping("/{id}")
    public ResponseEntity<RespostaApiDto<Void>> excluir(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.ok(new RespostaApiDto<>("Usuário excluído com sucesso!"));
=======
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarTodos() {
        List<UsuarioResponseDto> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespostaApiDto<Void>> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.ok(new RespostaApiDto<>("Usuário deletado com sucesso!", null));
>>>>>>> 2e66d3441d0026350e888c13eaacd8e148c1c33e
    }
}