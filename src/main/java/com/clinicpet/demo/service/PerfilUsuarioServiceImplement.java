package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilUsuario;
import com.clinicpet.demo.repository.IPerfilUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PerfilUsuarioServiceImplement implements IPerfilUsuarioService {

	@Autowired
	private IPerfilUsuarioRepository perfilUsuarioRepository;

	@Override
	@Transactional
	public PerfilUsuario crearPerfilUsuario(PerfilUsuario perfilUsuario) {
		return perfilUsuarioRepository.save(perfilUsuario);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<PerfilUsuario> obtenerPerfilUsuarioPorId(Integer id) {
		return perfilUsuarioRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public PerfilUsuario obtenerPerfilUsuarioPorUsuarioId(Integer usuarioId) {
		return perfilUsuarioRepository.findByUsuarioId(usuarioId);
	}

	@Override
	@Transactional(readOnly = true)
	public PerfilUsuario obtenerPerfilUsuarioPorUsername(String username) {
		return perfilUsuarioRepository.findByUsuarioUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilUsuario> obtenerTodosLosPerfilesUsuarios() {
		return perfilUsuarioRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilUsuario> obtenerPerfilesPorDireccion(String direccion) {
		return perfilUsuarioRepository.findByDireccionContaining(direccion);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilUsuario> obtenerPerfilesConMascotas() {
		return perfilUsuarioRepository.findPerfilesConMascotas();
	}

	@Override
	@Transactional
	public PerfilUsuario actualizarPerfilUsuario(Integer id, PerfilUsuario perfilUsuario) {
		if (perfilUsuarioRepository.existsById(id)) {
			perfilUsuario.setId(id);
			return perfilUsuarioRepository.save(perfilUsuario);
		}
		throw new RuntimeException("Perfil Usuario no encontrado con ID: " + id);
	}

	@Override
	@Transactional
	public void eliminarPerfilUsuario(Integer id) {
		if (perfilUsuarioRepository.existsById(id)) {
			perfilUsuarioRepository.deleteById(id);
		} else {
			throw new RuntimeException("Perfil Usuario no encontrado con ID: " + id);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existePerfilUsuarioPorUsuarioId(Integer usuarioId) {
		return perfilUsuarioRepository.existsByUsuarioId(usuarioId);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existePerfilUsuarioPorId(Integer id) {
		return perfilUsuarioRepository.existsById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public long contarMascotasPorPerfil(Integer perfilId) {
		Optional<PerfilUsuario> perfil = perfilUsuarioRepository.findById(perfilId);
		return perfil.map(p -> p.getMascota().size()).orElse(0);
	}
}