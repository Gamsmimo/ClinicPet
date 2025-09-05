package com.clinicpet.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Calificacion;

@Repository
public interface ICalificacionRepository extends JpaRepository<Calificacion, Integer> {

//buscar por usuario que califica 	
	List<Calificacion> findByUsuario(Integer ussuarioId);

	// buscar por calificación a veterinarios
	List<Calificacion> findByVeterinario(Integer veterinarioId);

	// buscar por servicio calificado
	List<Calificacion> findByServicioId(Integer ServicioId);

	// buscar por fecha
	List<Calificacion> findByFecha(LocalDate fecha);

}
