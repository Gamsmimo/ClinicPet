package com.clinicpet.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.clinicpet.demo.model.Emergencia;

public interface IEmergenciaService {

// Guardar/actualizar una emergencia
	public Emergencia guardarEmergencia(Emergencia emergencia);

// Obtener todas las emergencias
	public List<Emergencia> obtenerTodasLasEmergencias();

// Obtener una emergencia por ID
	public Optional<Emergencia> obtenerEmergenciaPorId(Integer id);

// Eliminar una emergencia por ID
	public void eliminarEmergencia(Integer id);

	public List<Emergencia> obtenerEmergenciasPorTipo(String tipo);

	public List<Emergencia> obtenerEmergenciasPorMascota(Integer mascotaId);

	public List<Emergencia> obtenerEmergenciasPorVeterinario(Integer veterinarioId);

	public List<Emergencia> obtenerEmergenciasPorMascotaYRangoFechas(Integer mascotaId, LocalDateTime inicio,
			LocalDateTime fin);

// Métodos adicionales para gestión de emergencias
	public List<Emergencia> obtenerEmergenciasRecientes();

	public List<Emergencia> obtenerEmergenciasDeHoy();

	public List<Emergencia> obtenerEmergenciasPorRangoFechas(LocalDateTime inicio, LocalDateTime fin);
}
