package com.clinicpet.demo.service;

import com.clinicpet.demo.model.Usuario;
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
			usuario.setUsername(usuarioActualizado.getUsername());
			usuario.setNombre(usuarioActualizado.getNombre());
			usuario.setApellidos(usuarioActualizado.getApellidos());
			usuario.setCorreo(usuarioActualizado.getCorreo());
			usuario.setTipoDocumento(usuarioActualizado.getTipoDocumento());
			usuario.setNumDocumento(usuarioActualizado.getNumDocumento());
			usuario.setTelefono(usuarioActualizado.getTelefono());
			usuario.setEdad(usuarioActualizado.getEdad());
			usuario.setContraseña(usuarioActualizado.getContraseña()); // 👈 ojo aquí
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
	public Optional<Usuario> buscarUsuarioPorUsername(String username) {
		return usuarioRepository.findByUsername(username);
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
	public List<Usuario> buscarUsuariosPorNombreOApellido(String nombre, String apellidos) {
		return usuarioRepository.findByNombreContainingOrApellidosContaining(nombre, apellidos);
	}

	@Override
	public boolean existeUsername(String username) {
		return usuarioRepository.existsByUsername(username);
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

	@Override
	public boolean validarCredenciales(String username, String contraseña) {
		return usuarioRepository.findByUsername(username).map(usuario -> usuario.getContraseña().equals(contraseña)) // 👈
																														// aquí
																														// también
				.orElse(false);
	}
}
