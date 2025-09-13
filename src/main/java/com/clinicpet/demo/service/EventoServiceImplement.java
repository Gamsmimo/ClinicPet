package com.clinicpet.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinicpet.demo.model.Evento;
import com.clinicpet.demo.repository.IEventoRepository;

@Service
public class EventoServiceImplement implements IEventoService {

	@Autowired
	private IEventoRepository eventoRepository;

	@Override
	public Evento guardarEvento(Evento evento) {
		// TODO Auto-generated method stub
		validarEvento(evento);
		return eventoRepository.save(evento);
	}

	@Override
	public List<Evento> obtenerTodosLosEventos() {
		// TODO Auto-generated method stub
		return eventoRepository.findAll();
	}

	@Override
	public Optional<Evento> obtenerEventoPorId(Integer id) {
		// TODO Auto-generated method stub
		return eventoRepository.findById(id);
	}

	@Override
	public void eliminarEvento(Integer id) {
		// TODO Auto-generated method stub
		if (!eventoRepository.existsById(id)) {
			throw new IllegalArgumentException("No existe un evento con el ID: " + id);
		}
		eventoRepository.deleteById(id);
	}

	@Override
	public List<Evento> obtenerEventosPorVeterinaria(Integer veterinariaId) {
		// TODO Auto-generated method stub
		return eventoRepository.findByVeterinaria_Id(veterinariaId);
	}

	@Override
	public List<Evento> buscarEventosPorTitulo(String titulo) {
		// TODO Auto-generated method stub
		return eventoRepository.findByTituloContainingIgnoreCase(titulo);
	}

	@Override
	public List<Evento> obtenerEventosEntreFechas(LocalDate inicio, LocalDate fin) {
		// TODO Auto-generated method stub
		return eventoRepository.findByFechainicioBetween(inicio, fin);
	}

	@Override
	public List<Evento> obtenerEventosPorFechaInicio(LocalDate fechaInicio) {
		// TODO Auto-generated method stub
		return eventoRepository.findByFechainicio(fechaInicio);
	}

	@Override
	public List<Evento> obtenerEventosPorFechaFin(LocalDate fechaFin) {
		// TODO Auto-generated method stub
		return eventoRepository.findByFechafin(fechaFin);
	}

	@Override
	public List<Evento> obtenerEventosActivos(LocalDate fechaActual) {
		// TODO Auto-generated method stub
		return eventoRepository.findByFechafinAfter(fechaActual);
	}

	@Override
	public List<Evento> obtenerEventosActivosHoy() {
		// TODO Auto-generated method stub
		LocalDate hoy = LocalDate.now();
		return obtenerTodosLosEventos().stream()
				.filter(evento -> !hoy.isBefore(evento.getFechainicio()) && !hoy.isAfter(evento.getFechafin()))
				.toList();
	}

	@Override
	public List<Evento> obtenerProximosEventos() {
		// TODO Auto-generated method stub
		LocalDate hoy = LocalDate.now();
		return obtenerTodosLosEventos().stream().filter(evento -> evento.getFechainicio().isAfter(hoy)).toList();
	}

	@Override
	public List<Evento> obtenerEventosVigentes() {
		// TODO Auto-generated method stub
		LocalDate hoy = LocalDate.now();
		return obtenerTodosLosEventos().stream().filter(evento -> !evento.getFechafin().isBefore(hoy)).toList();
	}

	@Override
	public List<Evento> obtenerEventosExpirados() {
		// TODO Auto-generated method stub
		LocalDate hoy = LocalDate.now();

		return obtenerTodosLosEventos().stream().filter(evento -> evento.getFechafin().isBefore(hoy)).toList();
	}

	@Override
	public List<Evento> obtenerEventosPorVeterinariaYVigentes(Integer veterinariaId) {
		// TODO Auto-generated method stub
		LocalDate hoy = LocalDate.now();
		List<Evento> eventosVeterinaria = obtenerEventosPorVeterinaria(veterinariaId);

		return eventosVeterinaria.stream().filter(evento -> !evento.getFechafin().isBefore(hoy)).toList();
	}

	// Método para validaciones
	private void validarEvento(Evento evento) {
		if (evento.getTitulo() == null || evento.getTitulo().trim().isEmpty()) {
			throw new IllegalArgumentException("El título del evento es obligatorio");
		}

		if (evento.getDescripcion() == null || evento.getDescripcion().trim().isEmpty()) {
			throw new IllegalArgumentException("La descripción del evento es obligatoria");
		}

		if (evento.getFechainicio() == null) {
			throw new IllegalArgumentException("La fecha de inicio es obligatoria");
		}

		if (evento.getFechafin() == null) {
			throw new IllegalArgumentException("La fecha de fin es obligatoria");
		}

		if (evento.getVeterinaria() == null) {
			throw new IllegalArgumentException("La veterinaria es obligatoria");
		}

		// Validar que la fecha de fin no sea anterior a la fecha de inicio
		if (evento.getFechafin().isBefore(evento.getFechainicio())) {
			throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
		}

		// Validar que las fechas no sean pasadas
		if (evento.getFechainicio().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("La fecha de inicio no puede ser en el pasado");
		}

		// Validar longitud máxima del título (opcional)
		if (evento.getTitulo().length() > 100) {
			throw new IllegalArgumentException("El título no puede exceder los 100 caracteres");
		}
	}

	public List<Evento> buscarEventosPorVeterinariaYTitulo(Integer veterinariaId, String titulo) {
		List<Evento> eventosVeterinaria = obtenerEventosPorVeterinaria(veterinariaId);

		return eventosVeterinaria.stream()
				.filter(evento -> evento.getTitulo().toLowerCase().contains(titulo.toLowerCase())).toList();
	}
}
