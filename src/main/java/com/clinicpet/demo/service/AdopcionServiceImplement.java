package com.clinicpet.demo.service;

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
	@Override
	public Adopcion guardarAdopcion(Adopcion adopcion) {
		return adopcionRepository.save(adopcion);
	}

	@Override
	public List<Adopcion> listarAdopciones() {
		return adopcionRepository.findAll();
	}

	@Override
	public Optional<Adopcion> buscarAdopcionById(Integer id) {
		return adopcionRepository.findById(id);
	}

	@Override
	public void eliminarAdopcion(Integer id) {
		adopcionRepository.deleteById(id);

	}

	// METODOS PERSONALIZADOS
	@Override
	public List<Adopcion> buscarAdopcionesByEstado(String estado) {
		return adopcionRepository.findByEstado(estado);
	}

	@Override
	public List<Adopcion> buscarAdopcionesByVeterinaria(Integer idVeterinaria) {
		return adopcionRepository.findByVeterinariaId(idVeterinaria);
	}

	@Override
	public List<Adopcion> buscarAdopcionesByUsuarioAdoptante(Integer idUsuario) {
		return adopcionRepository.findByUsuarioAdoptanteId(idUsuario);
	}

	@Override
	public Optional<Adopcion> buscarAdopcionesByMascota(Integer idMascota) {
		return adopcionRepository.findByMascotaId(idMascota);
	}

}