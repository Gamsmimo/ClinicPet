package com.clinicpet.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Evento;
import java.time.LocalDateTime;

@Repository
public interface IEventoRepository extends JpaRepository<Evento, Integer> {

	// Busca evento por titulo ignorando mayusculas/minusculas.
	List<Evento> findByTituloContainingIgnoreCase(String titulo);

	List<Evento> findByFecha(LocalDateTime fecha);

	// Buscar entre 2 fechas.
	List<Evento> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

	// Buscar proximos eventos.
	List<Evento> findByFechaAfter(LocalDateTime fecha);

	// Buscar eventos pasados.
	List<Evento> findByFechaBefore(LocalDateTime fecha);
}
