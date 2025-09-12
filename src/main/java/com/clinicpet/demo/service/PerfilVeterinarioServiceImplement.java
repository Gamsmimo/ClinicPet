package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.repository.IPerfilVeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PerfilVeterinarioServiceImplement implements IPerfilVeterinarioService {

	@Autowired
	private IPerfilVeterinarioRepository perfilVeterinarioRepository;

	@Override
	@Transactional
	public PerfilVeterinario crearPerfilVeterinario(PerfilVeterinario perfilVeterinario) {
		return perfilVeterinarioRepository.save(perfilVeterinario);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorId(Integer id) {
		return perfilVeterinarioRepository.findById(id);
	}

	@Override
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
		return perfilVeterinarioRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerVeterinariosPorEspecialidad(String especialidad) {
		return perfilVeterinarioRepository.findByEspecialidad(especialidad);
	}

	@Override
	@Transactional(readOnly = true)
	public PerfilVeterinario obtenerVeterinarioPorRut(String rut) {
		return perfilVeterinarioRepository.findByRut(rut);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerVeterinariosPorTelefono(String telefono) {
		return perfilVeterinarioRepository.findByTelefono(telefono);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerVeterinariosDisponibles() {
		return perfilVeterinarioRepository.findVeterinariosDisponibles();
	}

	@Override
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
	public boolean existePerfilVeterinarioPorUsuarioId(Integer usuarioId) {
		return perfilVeterinarioRepository.existsByUsuarioId(usuarioId);
	}

	@Override
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