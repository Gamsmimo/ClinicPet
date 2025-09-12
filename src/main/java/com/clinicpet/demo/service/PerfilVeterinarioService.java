package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.repository.IPerfilVeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PerfilVeterinarioService implements IPerfilVeterinarioService {

	@Autowired
	private IPerfilVeterinarioRepository perfilVeterinarioRepository;

	@Override
	public PerfilVeterinario crearPerfilVeterinario(PerfilVeterinario perfilVeterinario) {
		return perfilVeterinarioRepository.save(perfilVeterinario);
	}

	@Override
	public Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorId(Integer id) {
		return perfilVeterinarioRepository.findById(id);
	}

	@Override
	public List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinario() {
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
		return perfilVeterinarioRepository.findByEspecialidad(especialidad);
	}

	@Override
	public Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorRut(String rut) {
		return Optional.ofNullable(perfilVeterinarioRepository.findByRut(rut));
	}

	@Override
	public List<PerfilVeterinario> obtenerPerfilesVeterinarioPorTelefono(String telefono) {
		return perfilVeterinarioRepository.findByTelefono(telefono);
	}

	@Override
	public List<PerfilVeterinario> obtenerVeterinariosDisponibles() {
		return perfilVeterinarioRepository.findVeterinariosDisponibles();
	}

	@Override
	public boolean existePerfilVeterinarioPorUsuarioId(Integer usuarioId) {
		return perfilVeterinarioRepository.existsByUsuarioId(usuarioId);
	}

	@Override
	public boolean existePerfilVeterinarioPorRut(String rut) {
		return perfilVeterinarioRepository.existsByRut(rut);
	}
}