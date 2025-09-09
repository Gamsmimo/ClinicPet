package com.clinicpet.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.clinicpet.demo.model.Adopcion;

public interface IAdopcionService {

	Adopcion guardarAdopcion(Adopcion adopcion);

	List<Adopcion> listarAdopciones();

	Optional<Adopcion> buscarPorId(Integer id);

	void eliminarAdopcion(Integer id);

	List<Adopcion> buscarPorEstado(String estado);

	List<Adopcion> buscarPorMascota(Integer mascotaId);

	List<Adopcion> buscarPorUsuarioAdoptante(Integer usuarioAdoptante);

	List<Adopcion> buscarPorVeterinaria(Integer veterinariaId);

	List<Adopcion> buscarPorFechaSolicitud(Date fechaSolicitud);
}
