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
		// verificar que fechafin no sea anterior a fechainicio
		if (evento.getFechafin().isBefore(evento.getFechainicio())) {
			throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
		}
		return eventoRepository.save(evento);
	}

	@Override
	public List<Evento> obtenerTodosLosEventos() {
		// TODO Auto-generated method stub
		return eventoRepository.findAll();
	}

	@Override
	public Optional<Evento> obtenerEventoporId(Integer id) {
		// TODO Auto-generated method stub
		return eventoRepository.findById(id);
	}

	@Override
	public void eliminarEvento(Integer id) {
		// TODO Auto-generated method stub
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
		return eventoRepository.findByFechafinAfter(fechaFin);
	}

	@Override
	public List<Evento> obtenerEventosActivos(LocalDate fechaActual) {
		// TODO Auto-generated method stub
		return eventoRepository.findByFechafinAfter(fechaActual);
	}

	public List<Evento> obtenerEventosActivosHoy() {
		return obtenerEventosActivos(LocalDate.now());
	}

}
