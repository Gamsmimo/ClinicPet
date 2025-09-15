package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.repository.IPerfilVeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
=======
import org.springframework.transaction.annotation.Transactional;

>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa
import java.util.List;
import java.util.Optional;

@Service
public class PerfilVeterinarioServiceImplement implements IPerfilVeterinarioService {

	@Autowired
	private IPerfilVeterinarioRepository perfilVeterinarioRepository;

	@Override
<<<<<<< HEAD
=======
	@Transactional
>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa
	public PerfilVeterinario crearPerfilVeterinario(PerfilVeterinario perfilVeterinario) {
		return perfilVeterinarioRepository.save(perfilVeterinario);
	}

	@Override
<<<<<<< HEAD
=======
	@Transactional(readOnly = true)
>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa
	public Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorId(Integer id) {
		return perfilVeterinarioRepository.findById(id);
	}

	@Override
<<<<<<< HEAD
	public List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinario() {
=======
	@Transactional(readOnly = true)
	public PerfilVeterinario obtenerPerfilVeterinarioPorUsuarioId(Integer usuarioId) {
		return perfilVeterinarioRepository.findByUsuarioId(usuarioId);
	}

	@Override
	@Transactional(readOnly = true)
	public PerfilVeterinario obtenerPerfilVeterinarioPorUsername(String username) {
		return perfilVeterinarioRepository.findByUsuarioUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinarios() {
>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa
		return perfilVeterinarioRepository.findAll();
	}

	@Override
<<<<<<< HEAD
	public PerfilVeterinario actualizarPerfilVeterinario(Integer id, PerfilVeterinario perfilVeterinario) {
		if (perfilVeterinarioRepository.existsById(id)) {
			perfilVeterinario.setId(id);
			return perfilVeterinarioRepository.save(perfilVeterinario);
		}
		return null;
	}

	@Override
	public void eliminarPerfilVeterinario(Integer id) {
		perfilVeterinarioRepository.deleteById(id);
	}

	@Override
	public Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorUsuarioId(Integer usuarioId) {
		return Optional.ofNullable(perfilVeterinarioRepository.findByUsuarioId(usuarioId));
	}

	@Override
	public Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorUsername(String username) {
		return Optional.ofNullable(perfilVeterinarioRepository.findByUsuarioUsername(username));
	}

	@Override
	public List<PerfilVeterinario> obtenerPerfilesVeterinarioPorEspecialidad(String especialidad) {
=======
	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerVeterinariosPorEspecialidad(String especialidad) {
>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa
		return perfilVeterinarioRepository.findByEspecialidad(especialidad);
	}

	@Override
<<<<<<< HEAD
	public Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorRut(String rut) {
		return Optional.ofNullable(perfilVeterinarioRepository.findByRut(rut));
	}

	@Override
	public List<PerfilVeterinario> obtenerPerfilesVeterinarioPorTelefono(String telefono) {
=======
	@Transactional(readOnly = true)
	public PerfilVeterinario obtenerVeterinarioPorRut(String rut) {
		return perfilVeterinarioRepository.findByRut(rut);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerVeterinariosPorTelefono(String telefono) {
>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa
		return perfilVeterinarioRepository.findByTelefono(telefono);
	}

	@Override
<<<<<<< HEAD
=======
	@Transactional(readOnly = true)
>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa
	public List<PerfilVeterinario> obtenerVeterinariosDisponibles() {
		return perfilVeterinarioRepository.findVeterinariosDisponibles();
	}

	@Override
<<<<<<< HEAD
=======
	@Transactional
	public PerfilVeterinario actualizarPerfilVeterinario(Integer id, PerfilVeterinario perfilVeterinario) {
		if (perfilVeterinarioRepository.existsById(id)) {
			perfilVeterinario.setId(id);
			return perfilVeterinarioRepository.save(perfilVeterinario);
		}
		throw new RuntimeException("Perfil Veterinario no encontrado con ID: " + id);
	}

	@Override
	@Transactional
	public void eliminarPerfilVeterinario(Integer id) {
		if (perfilVeterinarioRepository.existsById(id)) {
			perfilVeterinarioRepository.deleteById(id);
		} else {
			throw new RuntimeException("Perfil Veterinario no encontrado con ID: " + id);
		}
	}

	@Override
	@Transactional(readOnly = true)
>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa
	public boolean existePerfilVeterinarioPorUsuarioId(Integer usuarioId) {
		return perfilVeterinarioRepository.existsByUsuarioId(usuarioId);
	}

	@Override
<<<<<<< HEAD
	public boolean existePerfilVeterinarioPorRut(String rut) {
		return perfilVeterinarioRepository.existsByRut(rut);
	}
}
=======
	@Transactional(readOnly = true)
	public boolean existePerfilVeterinarioPorId(Integer id) {
		return perfilVeterinarioRepository.existsById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existeVeterinarioPorRut(String rut) {
		return perfilVeterinarioRepository.existsByRut(rut);
	}

	@Override
	@Transactional(readOnly = true)
	public long contarCitasPorVeterinario(Integer veterinarioId) {
		Optional<PerfilVeterinario> veterinario = perfilVeterinarioRepository.findById(veterinarioId);
		return veterinario.map(v -> v.getCita().size()).orElse(0);
	}
}
>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa
