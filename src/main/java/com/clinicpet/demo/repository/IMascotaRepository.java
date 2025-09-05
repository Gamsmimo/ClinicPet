package com.clinicpet.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Mascota;

@Repository
public interface IMascotaRepository extends JpaRepository<Mascota, Integer> {

	// buscar por usuario
	List<Mascota> findByUsuarioId(Integer usuarioId);

	// buscar por estado
	List<Mascota> findByEstado(String estado);

	// buscar mascota por especie
	List<Mascota> findByEspecie(String especie);

}
