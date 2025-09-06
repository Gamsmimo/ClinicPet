package com.clinicpet.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Adopcion;

@Repository
public interface IAdopcionRepository extends JpaRepository<Adopcion, Integer> {

	// busacr por estado
	List<Adopcion> findByEstado(String estado);

	// busacr por id de la mascota
	List<Adopcion> findByMascota_Id(Integer mascotaId);

	// buscar por usuario adoptante
	List<Adopcion> findByUsuarioAdoptante_Id(Integer usuarioAdoptante);

	// buscar por veterinaria
	List<Adopcion> findByVeterinaria_Id(Integer veterinariaId);

	// buscar por fecha de solicitud
	List<Adopcion> findByFechaSolicitud(Date fechaSolicitud);
}