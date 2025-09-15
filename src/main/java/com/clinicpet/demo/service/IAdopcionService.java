package com.clinicpet.demo.service;

import java.util.List;
import java.util.Optional;

import com.clinicpet.demo.model.Adopcion;

public interface IAdopcionService {

	// CRUD
	Adopcion guardarAdopcion(Adopcion adopcion);

	List<Adopcion> listarAdopciones();

	Optional<Adopcion> buscarAdopcionById(Integer id);

	void eliminarAdopcion(Integer id);

	// METODOS PERSONALIZADOS
	List<Adopcion> buscarAdopcionesByEstado(String estado);

	// Obtener adopciones por veterinaria
	List<Adopcion> buscarAdopcionesByVeterinaria(Integer idVeterinaria);

	// Obtener adopciones por usuario adoptante
	List<Adopcion> buscarAdopcionesByUsuarioAdoptante(Integer idUsuario);

	// Obtener adopciones por mascota
	Optional<Adopcion> buscarAdopcionesByMascota(Integer idMascota);
}
