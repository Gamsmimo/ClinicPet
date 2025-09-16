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
	public Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorRut(String rut) {
		return Optional.ofNullable(perfilVeterinarioRepository.findByRut(rut));
	}

	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerVeterinariosPorTelefono(String telefono) {
		return perfilVeterinarioRepository.findByTelefono(telefono);
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
	public PerfilVeterinario obtenerVeterinarioPorRut(String rut) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminarPerfilVeterinario(Integer id) {
	}

}
