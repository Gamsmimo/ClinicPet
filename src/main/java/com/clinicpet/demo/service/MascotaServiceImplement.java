package com.clinicpet.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinicpet.demo.model.Mascota;
import com.clinicpet.demo.repository.IMascotaRepository;

@Service
public class MascotaServiceImplement implements IMascotaService {

	@Autowired
	private IMascotaRepository mascotaRepository;

	@Override
	@Transactional
	public Mascota guardarMascota(Mascota mascota) {
		// Tus validaciones originales (sin cambios)
		if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
			throw new RuntimeException("Nombre requerido");
		}
		if (mascota.getEspecie() == null || mascota.getEspecie().trim().isEmpty()) {
			throw new RuntimeException("Especie requerida");
		}
		if (mascota.getEdad() == null || mascota.getEdad() < 0) {
			throw new RuntimeException("Edad positiva requerida");
		}
		if (mascota.getUsuario() == null || mascota.getUsuario().getId() == null) {
			throw new RuntimeException("Usuario requerido");
		}

		// Tus trims originales (sin cambios)
		mascota.setNombre(mascota.getNombre().trim());
		mascota.setEspecie(mascota.getEspecie().trim());
		if (mascota.getRaza() != null)
			mascota.setRaza(mascota.getRaza().trim());

		// Tus logs originales (sin cambios)
		System.out.println("DEBUG SERVICE: Guardando mascota '" + mascota.getNombre() + "' con FK idUsuario = "
				+ mascota.getUsuario().getId());

		Mascota saved = mascotaRepository.save(mascota);

		System.out.println("DEBUG SERVICE: Mascota guardada con ID = " + saved.getId() + ", FK idUsuario = "
				+ saved.getUsuario().getId());

		return saved;
	}

	@Override
	public List<Mascota> listarMascotas() {
		return mascotaRepository.findAll();
	}

	@Override
	public Optional<Mascota> buscarMascotaPorId(Integer id) {
		if (id == null)
			return Optional.empty();
		return mascotaRepository.findById(id); // Sin cambios
	}

	@Override
	@Transactional
	public void eliminarMascota(Integer id) {
		// Corrección: Agregar checks para robustez (lanza excepción si falla)
		if (id == null) {
			throw new RuntimeException("ID de mascota requerido");
		}
		if (!mascotaRepository.existsById(id)) {
			throw new RuntimeException("Mascota no encontrada con ID: " + id);
		}
		mascotaRepository.deleteById(id);
		// Agregado: Log para debug
		System.out.println("DEBUG SERVICE: Mascota eliminada con ID = " + id);
	}

	// Corrección principal: Implementación agregada de actualizarMascota (faltaba)
	@Override
	@Transactional
	public Mascota actualizarMascota(Mascota mascota) {
		if (mascota == null || mascota.getId() == null) {
			throw new RuntimeException("Mascota e ID requeridos para actualización");
		}
		// Verifica existencia
		Optional<Mascota> existing = mascotaRepository.findById(mascota.getId());
		if (existing.isEmpty()) {
			throw new RuntimeException("Mascota no encontrada para actualizar");
		}

		// Actualiza campos (mantén FK usuario por defecto)
		Mascota toUpdate = existing.get();
		if (mascota.getNombre() != null)
			toUpdate.setNombre(mascota.getNombre().trim());
		if (mascota.getEspecie() != null)
			toUpdate.setEspecie(mascota.getEspecie().trim());
		if (mascota.getEdad() != null)
			toUpdate.setEdad(mascota.getEdad());
		if (mascota.getRaza() != null)
			toUpdate.setRaza(mascota.getRaza().trim());
		// Si quieres actualizar usuario: toUpdate.setUsuario(mascota.getUsuario());

		// Logs para debug
		System.out.println("DEBUG SERVICE: Actualizando mascota ID=" + mascota.getId() + ", nombre='"
				+ toUpdate.getNombre() + "'");

		Mascota updated = mascotaRepository.save(toUpdate);
		System.out.println("DEBUG SERVICE: Mascota actualizada con ID = " + updated.getId());

		return updated;
	}

	@Override
	public List<Mascota> buscarPorUsuario(Integer usuarioId) {
		// Sin cambios: Tus logs y método
		if (usuarioId == null) {
			System.out.println("DEBUG SERVICE: buscarPorUsuario - ID null, retorna vacío");
			return List.of();
		}

		List<Mascota> mascotas = mascotaRepository.findByUsuario_Id(usuarioId);

		String nombres = mascotas.stream().map(m -> m.getNombre()).collect(Collectors.toList()).toString();
		System.out.println("DEBUG SERVICE: buscarPorUsuario - ID=" + usuarioId + ", Encontradas: " + mascotas.size()
				+ " (Nombres: " + nombres + ")");

		return mascotas;
	}

	@Override
	public List<Mascota> buscarPorEspecie(String especie) {
		// Sin cambios
		if (especie == null || especie.trim().isEmpty())
			return List.of();
		return mascotaRepository.findByEspecie(especie.trim());
	}
}
