package com.clinicpet.demo.controller;

import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioApiController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/{id}/perfil")
    public ResponseEntity<Usuario> obtenerPerfilUsuario(@PathVariable Integer id) {
        return usuarioService.buscarUsuarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<Usuario> actualizarPerfilUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        if (!usuarioService.buscarUsuarioPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuario.setId(id);
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }
}
