package com.clinicpet.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Emergencia;
import java.time.LocalDateTime;

@Repository
public interface IEmergenciaRepository extends JpaRepository<Emergencia, Integer> {

	List<Emergencia> findByTipo(String tipo);

	List<Emergencia> findByMascotaId(Integer mascotaId);

	List<Emergencia> findByVeterinarioId(Integer veterinarioId);

	//Buscar emergencias en un rango de tiempo.
	List<Emergencia> findByMascotaIdAndFechaHoraBetween(Integer mascotaId, LocalDateTime inicio, LocalDateTime fin);

}
