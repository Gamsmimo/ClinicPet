package com.clinicpet.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Evento;

import java.time.LocalDate;

@Repository
public interface IEventoRepository extends JpaRepository<Evento, Integer> {
	
	//Buscar todos los eventos de una veterinaria
	List<Evento> findByVeterinaria_Id(Integer veterinariaId);

	// Busca evento por titulo ignorando mayusculas/minusculas.
	List<Evento> findByTituloContainingIgnoreCase(String titulo);

	// Buscar entre 2 fechas.
	List<Evento> findByFechainicioBetween(LocalDate inicio, LocalDate fin);

	// Buscar eventos en una fecha especifica.
	List<Evento> findByFechainicio(LocalDate fechainicio);

	List<Evento> findByFechafin(LocalDate fechafin);
	
	//Buscar eventos activos (que no han terminado aun).
	List<Evento> findByFechafinAfter(LocalDate fechaActual);
}
