package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilUsuario;
import com.clinicpet.demo.repository.IPerfilUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PerfilUsuarioService implements IPerfilUsuarioService {

	@Autowired
	private IPerfilUsuarioRepository perfilUsuarioRepository;

	@Override
	public PerfilUsuario crearPerfilUsuario(PerfilUsuario perfilUsuario) {
		return perfilUsuarioRepository.save(perfilUsuario);
	}

	@Override
	public Optional<PerfilUsuario> obtenerPerfilUsuarioPorId(Integer id) {
		return perfilUsuarioRepository.findById(id);
	}

	@Override
	public List<PerfilUsuario> obtenerTodosLosPerfilesUsuario() {
		return perfilUsuarioRepository.findAll();
	}

	@Override
	public PerfilUsuario actualizarPerfilUsuario(Integer id, PerfilUsuario perfilUsuario) {
		if (perfilUsuarioRepository.existsById(id)) {
			perfilUsuario.setId(id);
			return perfilUsuarioRepository.save(perfilUsuario);
		}
		return null;
	}

	@Override
	public void eliminarPerfilUsuario(Integer id) {
		perfilUsuarioRepository.deleteById(id);
	}

	@Override
	public Optional<PerfilUsuario> obtenerPerfilUsuarioPorUsuarioId(Integer usuarioId) {
		return Optional.ofNullable(perfilUsuarioRepository.findByUsuarioId(usuarioId));
	}

	@Override
	public Optional<PerfilUsuario> obtenerPerfilUsuarioPorUsername(String username) {
		return Optional.ofNullable(perfilUsuarioRepository.findByUsuarioUsername(username));
	}

	@Override
	public List<PerfilUsuario> obtenerPerfilesUsuarioPorDireccion(String direccion) {
		return perfilUsuarioRepository.findByDireccionContaining(direccion);
	}

	@Override
	public List<PerfilUsuario> obtenerPerfilesUsuarioPorTelefono(String telefono) {
		return perfilUsuarioRepository.findByTelefono(telefono);
	}

	@Override
	public List<PerfilUsuario> obtenerPerfilesUsuarioConMascotas() {
		return perfilUsuarioRepository.findPerfilesConMascotas();
	}

	@Override
	public boolean existePerfilUsuarioPorUsuarioId(Integer usuarioId) {
		return perfilUsuarioRepository.existsByUsuarioId(usuarioId);
	}
}