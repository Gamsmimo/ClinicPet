package com.clinicpet.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // *** AGREGADO: Para stream en log (opcional, quita si no usas Java 8+)

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
		// Validaciones básicas (mantengo tus checks - evitan FK null)
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
		if (mascota.getEstado() == null || mascota.getEstado().isEmpty()) {
			mascota.setEstado("disponible");
		}

		// Trim básico (mantengo)
		mascota.setNombre(mascota.getNombre().trim());
		mascota.setEspecie(mascota.getEspecie().trim());
		if (mascota.getRaza() != null)
			mascota.setRaza(mascota.getRaza().trim());
		mascota.setEstado(mascota.getEstado().trim());

		// *** AGREGADO: Log temporal para debug BD (confirma FK y save)
		System.out.println("DEBUG SERVICE: Guardando mascota '" + mascota.getNombre() + "' con FK idUsuario = "
				+ mascota.getUsuario().getId());

		Mascota saved = mascotaRepository.save(mascota);

		// *** AGREGADO: Log post-save (ver ID generado)
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
		return mascotaRepository.findById(id);
	}

	@Override
	@Transactional
	public void eliminarMascota(Integer id) {
		if (id != null && mascotaRepository.existsById(id)) {
			mascotaRepository.deleteById(id);
		}
	}

	@Override
	public List<Mascota> buscarPorUsuario(Integer usuarioId) {
		if (usuarioId == null) {
			System.out.println("DEBUG SERVICE: buscarPorUsuario - ID null, retorna vacío");
			return List.of();
		}

		// *** CORREGIDO: Cambiado a findByUsuario_Id (estándar JPA para usuario.id)
		List<Mascota> mascotas = mascotaRepository.findByUsuario_Id(usuarioId);

		// *** AGREGADO: Log temporal para debug (cuenta y nombres - verifica si carga
		// de BD)
		String nombres = mascotas.stream().map(m -> m.getNombre()).collect(Collectors.toList()).toString();
		System.out.println("DEBUG SERVICE: buscarPorUsuario - ID=" + usuarioId + ", Encontradas: " + mascotas.size()
				+ " (Nombres: " + nombres + ")");

		return mascotas;
	}

	@Override
	public List<Mascota> buscarPorEstado(String estado) {
		if (estado == null || estado.trim().isEmpty())
			return List.of();
		return mascotaRepository.findByEstado(estado.trim());
	}

	@Override
	public List<Mascota> buscarPorEspecie(String especie) {
		if (especie == null || especie.trim().isEmpty())
			return List.of();
		return mascotaRepository.findByEspecie(especie.trim());
	}
}
