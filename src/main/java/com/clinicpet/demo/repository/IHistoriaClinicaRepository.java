package com.clinicpet.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.HistoriaClinica;

@Repository
public interface IHistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Integer> {

	List<HistoriaClinica> findByMascota_MascotaId(Integer mascotaId);

	List<HistoriaClinica> findByVeterinario_UsuarioId(Integer veterinarioId);

	List<HistoriaClinica> findByUsuario_UsuarioId(Integer usuarioId);

	// Buscar historias clinicas en una veterinaria especifica
	List<HistoriaClinica> findByVeterinaria_VeterinariaId(Integer veterinariaId);

	// Buscar historias clinicas de mascotas en un rango de fechas.
	List<HistoriaClinica> findByMascota_MascotaIdAndFevhaBetween(Integer mascotaId, LocalDateTime inicio,
			LocalDateTime fin);

}
