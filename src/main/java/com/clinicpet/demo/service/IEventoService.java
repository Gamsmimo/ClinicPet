package com.clinicpet.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.clinicpet.demo.model.Evento;

public interface IEventoService {

	// Guardar/actualizar un evento
	public Evento guardarEvento(Evento evento);

	// Obtener todos los eventos
	public List<Evento> obtenerTodosLosEventos();

	// Obtener un evento por ID
	public Optional<Evento> obtenerEventoPorId(Integer id);

	// Eliminar un evento por ID
	public void eliminarEvento(Integer id);

	public List<Evento> obtenerEventosPorVeterinaria(Integer veterinariaId);

	public List<Evento> buscarEventosPorTitulo(String titulo);

	public List<Evento> obtenerEventosEntreFechas(LocalDate inicio, LocalDate fin);

	public List<Evento> obtenerEventosPorFechaInicio(LocalDate fechaInicio);

	public List<Evento> obtenerEventosPorFechaFin(LocalDate fechaFin);

	public List<Evento> obtenerEventosActivos(LocalDate fechaActual);

	// Métodos adicionales para gestión de eventos
	public List<Evento> obtenerEventosActivosHoy();

	public List<Evento> obtenerProximosEventos();

	public List<Evento> obtenerEventosVigentes();

	public List<Evento> obtenerEventosExpirados();

	public List<Evento> obtenerEventosPorVeterinariaYVigentes(Integer veterinariaId);
}
