package com.clinicpet.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.clinicpet.demo.model.Evento;

public interface IEventoService {

	public Evento guardarEvento(Evento evento);

	public List<Evento> obtenerTodosLosEventos();

	public Optional<Evento> obtenerEventoporId(Integer id);

	public void eliminarEvento(Integer id);

	public List<Evento> obtenerEventosPorVeterinaria(Integer veterinariaId);

	public List<Evento> buscarEventosPorTitulo(String titulo);

	public List<Evento> obtenerEventosEntreFechas(LocalDate inicio, LocalDate fin);

	public List<Evento> obtenerEventosPorFechaInicio(LocalDate fechaInicio);

	public List<Evento> obtenerEventosPorFechaFin(LocalDate fechaFin);

	public List<Evento> obtenerEventosActivos(LocalDate fechaActual);

}
