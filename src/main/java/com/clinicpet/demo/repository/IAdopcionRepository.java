package com.clinicpet.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Adopcion;

@Repository
public interface IAdopcionRepository extends JpaRepository<Adopcion, Integer> {

	// busacr por estado
	List<Adopcion> findByEstado(String estado);

	// busacr por id de la mascota
	List<Adopcion> finByMascotaId(Integer mascotaId);

	// buscar por usuario adoptante
	List<Adopcion> findByAdoptanteId(Integer usuarioId);

	// buscar por veterinaria
	List<Adopcion> finfByVeterinariaId(Integer veterinariaId);

	// buscar por fecha de solicitud
	List<Adopcion> finfByFechaSolicitud(LocalDate FechaSolicitud);
}