package com.clinicpet.demo.service;

import com.clinicpet.demo.model.Veterinaria;
import com.clinicpet.demo.repository.IVeterinariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VeterinariaServiceImplement implements IVeterinariaService {

	@Autowired
	private IVeterinariaRepository veterinariaRepository;

	@Override
	@Transactional
	public Veterinaria update(Veterinaria veterinaria) {
		// Verificar que la veterinaria existe antes de actualizar
		if (veterinaria.getId() != null && veterinariaRepository.existsById(veterinaria.getId())) {
			return veterinariaRepository.save(veterinaria);
		}
		throw new RuntimeException("Veterinaria no encontrada para actualizar");
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		veterinariaRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Veterinaria> findByNombre(String nombre) {
		return veterinariaRepository.findByNombre(nombre);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Veterinaria> findByNombreContainingIgnoreCase(String nombre) {
		return veterinariaRepository.findByNombreContainingIgnoreCase(nombre);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Veterinaria> findByDireccionContainingIgnoreCase(String direccion) {
		return veterinariaRepository.findByDireccionContainingIgnoreCase(direccion);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Veterinaria> findByCorreo(String correo) {
		return veterinariaRepository.findByCorreo(correo);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Veterinaria> findByTelefono(String telefono) {
		return veterinariaRepository.findByTelefono(telefono);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByNombre(String nombre) {
		return veterinariaRepository.existsByNombre(nombre);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByCorreo(String correo) {
		return veterinariaRepository.existsByCorreo(correo);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByTelefono(String telefono) {
		return veterinariaRepository.existsByTelefono(telefono);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Veterinaria> findByCiudad(String ciudad) {
		return veterinariaRepository.findByCiudad(ciudad);
	}

	@Override
	@Transactional(readOnly = true)
	public long countTotalVeterinarias() {
		return veterinariaRepository.count(); // Usa el método count() de JpaRepository
	}

	@Override
	@Transactional(readOnly = true)
	public List<Veterinaria> findByServicioNombre(String servicio) {
		return veterinariaRepository.findByServicioNombre(servicio);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Veterinaria> findByHorarioContaining(String horario) {
		return veterinariaRepository.findByHorarioContaining(horario);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Veterinaria> findAllOrderByNombreAsc() {
		return veterinariaRepository.findAllByOrderByNombreAsc();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Veterinaria> findByDescripcionContainingIgnoreCase(String descripcion) {
		return veterinariaRepository.findByDescripcionContainingIgnoreCase(descripcion);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Veterinaria> searchByMultipleFields(String keyword) {
		// Búsqueda en múltiples campos
		String searchTerm = keyword.toLowerCase();
		List<Veterinaria> resultados = veterinariaRepository.findByNombreContainingIgnoreCase(searchTerm);

		// También buscar por dirección si no hay resultados por nombre
		if (resultados.isEmpty()) {
			resultados = veterinariaRepository.findByDireccionContainingIgnoreCase(searchTerm);
		}

		return resultados;
	}

	@Override
	public boolean validateVeterinariaData(Veterinaria veterinaria) {
		// Validaciones básicas de datos
		if (veterinaria.getNombre() == null || veterinaria.getNombre().trim().isEmpty()) {
			return false;
		}
		if (veterinaria.getDireccion() == null || veterinaria.getDireccion().trim().isEmpty()) {
			return false;
		}
		if (veterinaria.getTelefono() == null || veterinaria.getTelefono().trim().isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Veterinaria> listarPendientes() {
		return veterinariaRepository.findByEstado("PENDIENTE");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Veterinaria> listarAprobadas() {
		return veterinariaRepository.findByEstado("APROBADA");
	}

	@Override
	@Transactional
	public Veterinaria aprobarVeterinaria(Integer id) {
		Veterinaria v = veterinariaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));
		v.setEstado("Aprobada");
		return veterinariaRepository.save(v);
	}

	@Override
	@Transactional
	public Veterinaria rechazarVeterinaria(Integer id) {
		Veterinaria v = veterinariaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));
		v.setEstado("Rechazada");
		return veterinariaRepository.save(v);
	}

	@Override
	@Transactional
	public Veterinaria save(Veterinaria veterinaria) {
		// Cuando se registra, por defecto queda PENDIENTE
		if (veterinaria.getEstado() == null) {
			veterinaria.setEstado("Pendiente");
		}
		return veterinariaRepository.save(veterinaria);
	}

	@Override
	@Transactional
	public Veterinaria findById(Integer id) {
		return veterinariaRepository.findById(id).orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Veterinaria> findAll() {
		return veterinariaRepository.findAll();
	}

	@Override
	public List<Veterinaria> listarPorEstado(String estado) {
		return veterinariaRepository.findByEstado(estado);
	}

	@Override
	@Transactional
	public Veterinaria desactivarVeterinaria(Integer id) {
		Veterinaria v = veterinariaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));
		v.setEstado("Inactiva"); // cambiar estado a inactiva
		return veterinariaRepository.save(v);
	}

	@Override
	public Veterinaria activarVeterinaria(Integer id) {
		Veterinaria v = findById(id);
		v.setEstado("Aprobada");
		return veterinariaRepository.save(v);
	}
}
