package com.clinicpet.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinicpet.demo.model.Emergencia;
import com.clinicpet.demo.repository.IEmergenciaRepository;

@Service
public class EmergenciaServiceImplement implements IEmergenciaService {

	@Autowired
	private IEmergenciaRepository emergenciaRepository;

	@Override
	public Emergencia guardarEmergencia(Emergencia emergencia) {
		// TODO Auto-generated method stub
		validarEmergencia(emergencia);
		// si no se especifica fecha y hora se establecera automaticamente.
		if (emergencia.getFechayhora() == null) {
			emergencia.setFechayhora(LocalDateTime.now());
		}
		return emergenciaRepository.save(emergencia);
	}

	@Override
	public List<Emergencia> obtenerTodasLasEmergencias() {
		// TODO Auto-generated method stub
		return emergenciaRepository.findAll();
	}

	@Override
	public Optional<Emergencia> obtenerEmergenciaPorId(Integer id) {
		// TODO Auto-generated method stub
		return emergenciaRepository.findById(id);
	}

	@Override
	public void eliminarEmergencia(Integer id) {
		// TODO Auto-generated method stub
		if (!emergenciaRepository.existsById(id)) {
			throw new IllegalArgumentException("No existe una emeregencia con el ID: " + id);
		}
		emergenciaRepository.deleteById(id);
	}

	@Override
	public List<Emergencia> obtenerEmergenciasPorTipo(String tipo) {
		// TODO Auto-generated method stub
		return emergenciaRepository.findByTipo(tipo);
	}

	@Override
	public List<Emergencia> obtenerEmergenciasPorMascota(Integer mascotaId) {
		// TODO Auto-generated method stub
		return emergenciaRepository.findByMascota_Id(mascotaId);
	}

	@Override
	public List<Emergencia> obtenerEmergenciasPorVeterinario(Integer veterinarioId) {
		// TODO Auto-generated method stub
		return emergenciaRepository.findByVeterinario_Id(veterinarioId);
	}

	@Override
	public List<Emergencia> obtenerEmergenciasPorMascotaYRangoFechas(Integer mascotaId, LocalDateTime inicio,
			LocalDateTime fin) {
		// TODO Auto-generated method stub
		return emergenciaRepository.findByMascota_IdAndFechayhoraBetween(mascotaId, inicio, fin);
	}

	@Override
	public List<Emergencia> obtenerEmergenciasRecientes() {
		// TODO Auto-generated method stub
		LocalDateTime hace24Horas = LocalDateTime.now().minusHours(24);
		return obtenerTodasLasEmergencias().stream().filter(e -> e.getFechayhora().isAfter(hace24Horas)).toList();
	}

	@Override
	public List<Emergencia> obtenerEmergenciasDeHoy() {
		// TODO Auto-generated method stub
		LocalDateTime inicioDelDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
		LocalDateTime finDelDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
		return obtenerTodasLasEmergencias().stream()
				.filter(e -> e.getFechayhora().isBefore(inicioDelDia) && !e.getFechayhora().isAfter(finDelDia))
				.toList();
	}

	@Override
	public List<Emergencia> obtenerEmergenciasPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
		// TODO Auto-generated method stub
		return obtenerTodasLasEmergencias().stream()
				.filter(e -> e.getFechayhora().isBefore(inicio) && !e.getFechayhora().isAfter(fin)).toList();
	}

	// Método para validaciones
	private void validarEmergencia(Emergencia emergencia) {
		if (emergencia.getTipo() == null || emergencia.getTipo().trim().isEmpty()) {
			throw new IllegalArgumentException("El tipo de emergencia es obligatorio");
		}

		if (emergencia.getDescripcion() == null || emergencia.getDescripcion().trim().isEmpty()) {
			throw new IllegalArgumentException("La descripción de la emergencia es obligatoria");
		}

		if (emergencia.getMascota() == null) {
			throw new IllegalArgumentException("La mascota es obligatoria");
		}

		if (emergencia.getVeterinario() == null) {
			throw new IllegalArgumentException("El veterinario es obligatorio");
		}

		if (emergencia.getVeterinaria() == null) {
			throw new IllegalArgumentException("La veterinaria es obligatoria");
		}

		// Validar que la fecha no sea futura
		if (emergencia.getFechayhora() != null && emergencia.getFechayhora().isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException("La fecha y hora de la emergencia no puede ser futura");
		}

		// Validar tipos de emergencia permitidos (puedes personalizar esta lista)
		List<String> tiposPermitidos = List.of("accidente", "enfermedad", "intoxicacion", "parto", "cirugia", "otro");
		if (!tiposPermitidos.contains(emergencia.getTipo().toLowerCase())) {
			throw new IllegalArgumentException(
					"Tipo de emergencia no válido: " + emergencia.getTipo() + ". Tipos permitidos: " + tiposPermitidos);
		}
	}

	// Método para obtener las emergencias mas recientes (ultimas 2 horas) que requieren atencion inmediata.
	public List<Emergencia> obtenerEmergenciasUrgentes() {
		// Definir qué se considera "urgente"
		LocalDateTime hace2Horas = LocalDateTime.now().minusHours(2);
		return obtenerTodasLasEmergencias().stream().filter(e -> e.getFechayhora().isAfter(hace2Horas)).toList();
	}

	// metodo para obtener rapidamente el numero total de emergencias del dia.
	public int contarEmergenciasHoy() {
		return obtenerEmergenciasDeHoy().size();
	}

}
