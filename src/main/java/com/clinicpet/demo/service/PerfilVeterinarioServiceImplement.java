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
	@Transactional
	public PerfilVeterinario findById(Integer id) {
		return perfilVeterinarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
	}

	@Transactional(readOnly = true)
	public PerfilVeterinario obtenerPerfilVeterinarioPorUsuarioId(Integer usuarioId) {
		return perfilVeterinarioRepository.findByUsuarioId(usuarioId);
	}

	@Override
	@Transactional(readOnly = true)
	public PerfilVeterinario obtenerPerfilVeterinarioPorNombres(String nombres) {
		return perfilVeterinarioRepository.findByUsuarioNombres(nombres);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinarios() {

		return perfilVeterinarioRepository.findAll();
	}

	@Override
	public PerfilVeterinario actualizarPerfilVeterinario(Integer id, PerfilVeterinario perfilVeterinario) {
		if (perfilVeterinarioRepository.existsById(id)) {
			perfilVeterinario.setId(id);
			return perfilVeterinarioRepository.save(perfilVeterinario);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerVeterinariosPorEspecialidad(String especialidad) {
		return perfilVeterinarioRepository.findByEspecialidad(especialidad);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerVeterinariosDisponibles() {
		return perfilVeterinarioRepository.findVeterinariosDisponibles();
	}

	@Transactional(readOnly = true)
	public boolean existePerfilVeterinarioPorId(Integer id) {
		return perfilVeterinarioRepository.existsById(id);
	}

	@Override
	public List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinario() {
		return perfilVeterinarioRepository.findAll();
	}

	@Override
	public PerfilVeterinario obtenerVeterinarioPorTarjetaProfesional(String tarjetaProfesional) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorTargetaProfesional(String tarjetaProfesional) {
		return Optional.empty();
	}

	@Override
	public List<PerfilVeterinario> ListarPorEstado(String estado) {
		return perfilVeterinarioRepository.findByEstado(estado);
	}

	@Override
	@Transactional
	public PerfilVeterinario desactivarVeterinario(Integer id) {
		PerfilVeterinario veterinario = perfilVeterinarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
		veterinario.setEstado("Inactiva"); // cambiar estado a inactiva
		return perfilVeterinarioRepository.save(veterinario);
	}

	@Override
	public PerfilVeterinario activarVeterinario(Integer id) {
		PerfilVeterinario veterinario = findById(id);
		veterinario.setEstado("Aprobada");
		return perfilVeterinarioRepository.save(veterinario);
	}

	@Override
	@Transactional
	public void aprobarVeterinario(Integer id) {
		PerfilVeterinario veterinario = perfilVeterinarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
		veterinario.setEstado("Aprobada"); // cambiar estado
		perfilVeterinarioRepository.save(veterinario); // guardar en la BD
	}

	@Override
	@Transactional
	public void rechazarVeterinario(Integer id) {
		PerfilVeterinario veterinario = perfilVeterinarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
		veterinario.setEstado("Rechazada"); // cambiar estado
		perfilVeterinarioRepository.save(veterinario); // guardar en la BD
	}

	@Override
	public void editarVeterinario(Integer id) {
	}

}
