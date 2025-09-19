package com.clinicpet.demo.service;

import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.model.Rol;
import com.clinicpet.demo.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImplement implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombres(usuarioActualizado.getNombres());
            usuario.setApellidos(usuarioActualizado.getApellidos());
            usuario.setCorreo(usuarioActualizado.getCorreo());
            usuario.setTipoDocumento(usuarioActualizado.getTipoDocumento());
            usuario.setNumDocumento(usuarioActualizado.getNumDocumento());
            usuario.setTelefono(usuarioActualizado.getTelefono());
            usuario.setEdad(usuarioActualizado.getEdad());
            usuario.setContraseña(usuarioActualizado.getContraseña());
            usuario.setDireccion(usuarioActualizado.getDireccion());
            usuario.setRol(usuarioActualizado.getRol());
            return usuarioRepository.save(usuario);
        }).orElse(null);
    }

    @Override
    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorNombres(String nombres) {
        return usuarioRepository.findByNombres(nombres);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorDocumento(String numDocumento) {
        return usuarioRepository.findByNumDocumento(numDocumento);
    }

    @Override
    public List<Usuario> buscarUsuariosPorRol(Integer rolId) {
        return usuarioRepository.findByRolId(rolId);
    }

    @Override
    public List<Usuario> buscarUsuariosPorNombresOApellidos(String nombres, String apellidos) {
        return usuarioRepository.findByNombresContainingOrApellidosContaining(nombres, apellidos);
    }

    @Override
    public boolean existeNombres(String nombres) {
        return usuarioRepository.existsByNombres(nombres);
    }

    @Override
    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    public boolean existeDocumento(String numDocumento) {
        return usuarioRepository.existsByNumDocumento(numDocumento);
    }

    @Override
    public void eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    // ================== NUEVOS MÉTODOS ==================

    @Override
    public Usuario activarUsuario(Integer id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setActivo(true);
            return usuarioRepository.save(usuario);
        }).orElse(null);
    }

    @Override
    public Usuario desactivarUsuario(Integer id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setActivo(false);
            return usuarioRepository.save(usuario);
        }).orElse(null);
    }

    @Override
    public Usuario cambiarRolUsuario(Integer id, Integer rolId) {
        return usuarioRepository.findById(id).map(usuario -> {
            Rol nuevoRol = new Rol();
            nuevoRol.setId(rolId);
            usuario.setRol(nuevoRol);
            return usuarioRepository.save(usuario);
        }).orElse(null);
    }

    @Override
    public boolean validarCredenciales(String nombres, String contraseña) {
        return usuarioRepository.findByNombres(nombres)
                .map(usuario -> usuario.getContraseña().equals(contraseña))
                .orElse(false);
    }
}

