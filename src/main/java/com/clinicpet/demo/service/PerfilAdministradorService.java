package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilAdministrador;
import com.clinicpet.demo.repository.IPerfilAdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PerfilAdministradorService implements IPerfilAdministradorService {

	@Autowired
	private IPerfilAdministradorRepository perfilAdministradorRepository;

	@Override
	public PerfilAdministrador crearPerfilAdministrador(PerfilAdministrador perfilAdministrador) {
		return perfilAdministradorRepository.save(perfilAdministrador);
	}

	@Override
	public Optional<PerfilAdministrador> obtenerPerfilAdministradorPorId(Integer id) {
		return perfilAdministradorRepository.findById(id);
	}

	@Override
	public List<PerfilAdministrador> obtenerTodosLosPerfilesAdministrador() {
		return perfilAdministradorRepository.findAll();
	}

	@Override
	public PerfilAdministrador actualizarPerfilAdministrador(Integer id, PerfilAdministrador perfilAdministrador) {
		if (perfilAdministradorRepository.existsById(id)) {
			perfilAdministrador.setId(id);
			return perfilAdministradorRepository.save(perfilAdministrador);
		}
		return null;
	}

	@Override
	public void eliminarPerfilAdministrador(Integer id) {
		perfilAdministradorRepository.deleteById(id);
	}

	@Override
	public Optional<PerfilAdministrador> obtenerPerfilAdministradorPorUsuarioId(Integer usuarioId) {
		return Optional.ofNullable(perfilAdministradorRepository.findByUsuarioId(usuarioId));
	}

	@Override
	public Optional<PerfilAdministrador> obtenerPerfilAdministradorPorUsername(String username) {
		return Optional.ofNullable(perfilAdministradorRepository.findByUsuarioUsername(username));
	}

	@Override
	public boolean existePerfilAdministradorPorUsuarioId(Integer usuarioId) {
		return perfilAdministradorRepository.existsByUsuarioId(usuarioId);
	}

	@Override
	public List<PerfilAdministrador> obtenerPerfilesAdministradorPorPermisos(String permiso) {
		return perfilAdministradorRepository.findByPermisosContaining(permiso);
	}
}