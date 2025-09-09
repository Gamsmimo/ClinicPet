package com.clinicpet.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinicpet.demo.model.Adopcion;
import com.clinicpet.demo.repository.IAdopcionRepository;

@Service
public class AdopcionServiceImplement implements IAdopcionService {

	@Autowired
	private IAdopcionRepository adopcionRepository;

	// CRUD

	// guardar las adopciones
	public Adopcion guardarAdopcion(Adopcion adopcion) {
		return adopcionRepository.save(adopcion);
	}

	// listar las adopciones
	public List<Adopcion> listarAdopciones() {
		return adopcionRepository.findAll();
	}

	// buscar las adopciones por id
	public Optional<Adopcion> buscarPorId(Integer id) {
		return adopcionRepository.findById(id);
	}

	// Eliminar adopción por id
	public void eliminarAdopcion(Integer id) {
		adopcionRepository.deleteById(id);
	}

	// METODOS PERSONALIZADOS JEJEJEJ
	// buscar adopción por estado
	@Override
	public List<Adopcion> buscarPorEstado(String estado) {
		return adopcionRepository.findByEstado(estado);
	}

	// buscar por mascota
	@Override
	public List<Adopcion> buscarPorMascota(Integer mascotaId) {
		return adopcionRepository.findByMascota_Id(mascotaId);
	}

//buscar por usuario adoptante 
	@Override
	public List<Adopcion> buscarPorUsuarioAdoptante(Integer usuarioAdoptante) {
		return adopcionRepository.findByUsuarioAdoptante_Id(usuarioAdoptante);
	}

	@Override
	public List<Adopcion> buscarPorVeterinaria(Integer veterinariaId) {
		return adopcionRepository.findByVeterinaria_Id(veterinariaId);
	}

	// buscar por fecha de solicitud
	@Override
	public List<Adopcion> buscarPorFechaSolicitud(Date fechaSolicitud) {
		return adopcionRepository.findByFechaSolicitud(fechaSolicitud);
	}
}
