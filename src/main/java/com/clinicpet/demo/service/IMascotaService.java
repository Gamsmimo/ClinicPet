package com.clinicpet.demo.service;

import java.util.List;
import java.util.Optional;

import com.clinicpet.demo.model.Mascota;

public interface IMascotaService {

	Mascota guardarMascota(Mascota mascota);

	List<Mascota> listarMascotas();

	Optional<Mascota> buscarMascotaPorId(Integer id);

	void eliminarMascota(Integer id);

	List<Mascota> buscarPorUsuario(Integer usuarioId);

	List<Mascota> buscarPorEstado(String estado);

	List<Mascota> buscarPorEspecie(String especie);

}
