package com.clinicpet.demo.service;

import java.util.List;
import java.util.Optional;

import com.clinicpet.demo.model.Mascota;

public interface IMascotaService {

	// CRUD
	Mascota guardarMascota(Mascota mascota);

	List<Mascota> listarMascotas();

	Optional<Mascota> buscarMascotaPorId(Integer id);

	void eliminarMascota(Integer id);

	// METODOS PERSONALIZADOS

	// Buscar por usuario
	List<Mascota> buscarPorUsuario(Integer usuarioId);

	// Buscar por estado
	List<Mascota> buscarPorEstado(String estado);

	// Buscar por especie
	List<Mascota> buscarPorEspecie(String especie);

}
