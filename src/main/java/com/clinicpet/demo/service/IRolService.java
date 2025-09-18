package com.clinicpet.demo.service;

import com.clinicpet.demo.model.Rol;
import com.clinicpet.demo.repository.IRolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IRolService {

	@Autowired
	private IRolRepository rolRepository;

	// CRUD básico
	@Transactional
	public Rol save(Rol rol) {
		return rolRepository.save(rol);
	}

	@Transactional(readOnly = true)
	public List<Rol> findAll() {
		return rolRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Rol> findById(Integer id) {
		return rolRepository.findById(id);
	}

	@Transactional
	public void deleteById(Integer id) {
		rolRepository.deleteById(id);
	}

	// Búsquedas personalizadas
	@Transactional(readOnly = true)
	public Optional<Rol> findByNombre(String nombre) {
		return rolRepository.findByNombre(nombre);
	}

	@Transactional(readOnly = true)
	public List<Rol> findByNombreContaining(String nombre) {
		return rolRepository.findByNombreContainingIgnoreCase(nombre);
	}

	@Transactional(readOnly = true)
	public boolean existsByNombre(String nombre) {
		return rolRepository.existsByNombre(nombre);
	}

	// Consultas personalizadas con JPQL
	@Transactional(readOnly = true)
	public List<Object[]> contarUsuariosPorRol() {
		return rolRepository.contarUsuariosPorRol();
	}

	@Transactional(readOnly = true)
	public List<Rol> findRolesConMasDeNUsuarios(int minUsuarios) {
		return rolRepository.findRolesConMasDeNUsuarios(minUsuarios);
	}

	@Transactional(readOnly = true)
	public List<Rol> findRolesOrdenadosPorCantidadUsuarios() {
		return rolRepository.findRolesOrdenadosPorCantidadUsuarios();
	}

	@Transactional(readOnly = true)
	public List<Rol> findRolesSinUsuarios() {
		return rolRepository.findRolesSinUsuarios();
	}

	@Transactional(readOnly = true)
	public List<Rol> findRolesConUsuariosAsignados() {
		return rolRepository.findRolesConUsuariosAsignados();
	}

	@Transactional(readOnly = true)
	public List<Rol> findRolesByIds(List<Integer> ids) {
		return rolRepository.findByIdIn(ids);
	}
}