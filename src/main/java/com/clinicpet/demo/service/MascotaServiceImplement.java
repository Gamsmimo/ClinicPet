package com.clinicpet.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		// Agregamos los demás campos que pueden ser actualizados
		if (mascota.getGenero() != null)
			toUpdate.setGenero(mascota.getGenero());
		if (mascota.getTamaño() != null)
			toUpdate.setTamaño(mascota.getTamaño());
		if (mascota.getDescripcion() != null)
			toUpdate.setDescripcion(mascota.getDescripcion().trim());
		// La foto se maneja en el controlador antes de llamar al servicio
		if (mascota.getFoto() != null)
			toUpdate.setFoto(mascota.getFoto());

		// Si quieres actualizar usuario: toUpdate.setUsuario(mascota.getUsuario()); //
		// Esto ya lo manejamos en el controlador para evitar perderlo

		// Logs para debug
		System.out.println("DEBUG SERVICE: Actualizando mascota ID=" + mascota.getId() + ", nombre='"
				+ toUpdate.getNombre() + "'");

		Mascota updated = mascotaRepository.save(toUpdate);
		System.out.println("DEBUG SERVICE: Mascota actualizada con ID = " + updated.getId());

		return updated;
	}

	@Override
	public Mascota guardarMascota(Mascota mascota) {
		return mascotaRepository.save(mascota);
	}

	@Override
	public List<Mascota> listarMascotas() {
		List<Mascota> mascotas = mascotaRepository.findAll();
		return mascotas != null ? mascotas : new ArrayList<>();
	}

	public Optional<Mascota> buscarMascotaPorId(Integer id) {
		return mascotaRepository.findById(id);
	}

	@Override
	public void eliminarMascota(Integer id) {
		if (id != null && mascotaRepository.existsById(id)) {
			mascotaRepository.deleteById(id);
		} else {
			throw new RuntimeException("Mascota no encontrada o ID inválido para eliminar");
		}
	}

	@Override
	public List<Mascota> buscarPorUsuario(Integer usuarioId) {
		List<Mascota> mascotas = mascotaRepository.findByUsuario_Id(usuarioId);
		return mascotas != null ? mascotas : new ArrayList<>();
	}

	@Override
	public List<Mascota> buscarPorEspecie(String especie) {
		if (especie == null || especie.trim().isEmpty()) {
			return new ArrayList<>();
		}
		List<Mascota> mascotas = mascotaRepository.findByEspecie(especie.trim());
		return mascotas != null ? mascotas : new ArrayList<>();
	}

}
