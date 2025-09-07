package com.clinicpet.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Emergencia;

@Repository
public interface IEmergenciaRepository extends JpaRepository<Emergencia, Integer> {

	List<Emergencia> findByTipo(String tipo);

	List<Emergencia> findByMascota_Id(Integer mascotaId);

	List<Emergencia> findByVeterinario_Id(Integer veterinarioId);

	// Buscar por rango de fechayHora
	List<Emergencia> findByMascota_IdAndFechayhoraBetween(Integer mascotaId, LocalDateTime inicio, LocalDateTime fin);
}
