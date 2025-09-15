package com.clinicpet.demo.controller;

import com.clinicpet.demo.model.Veterinaria;
import com.clinicpet.demo.service.IVeterinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/veterinarias")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen
public class VeterinariaController {

	@Autowired
	private IVeterinariaService veterinariaService;

	// GET: Obtener todas las veterinarias
	@GetMapping
	public ResponseEntity<List<Veterinaria>> getAllVeterinarias() {
		List<Veterinaria> veterinarias = veterinariaService.findAll();
		return new ResponseEntity<>(veterinarias, HttpStatus.OK);
	}

	// GET: Obtener veterinaria por ID
	@GetMapping("/{id}")
	public ResponseEntity<Veterinaria> getVeterinariaById(@PathVariable Integer id) {
		Optional<Veterinaria> veterinaria = veterinariaService.findById(id);
		return veterinaria.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// POST: Crear nueva veterinaria
	@PostMapping
	public ResponseEntity<Veterinaria> createVeterinaria(@RequestBody Veterinaria veterinaria) {
		if (!veterinariaService.validateVeterinariaData(veterinaria)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Veterinaria nuevaVeterinaria = veterinariaService.save(veterinaria);
		return new ResponseEntity<>(nuevaVeterinaria, HttpStatus.CREATED);
	}

	// PUT: Actualizar veterinaria existente
	@PutMapping("/{id}")
	public ResponseEntity<Veterinaria> updateVeterinaria(@PathVariable Integer id,
			@RequestBody Veterinaria veterinaria) {
		if (!veterinariaService.validateVeterinariaData(veterinaria)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		veterinaria.setId(id);
		Veterinaria veterinariaActualizada = veterinariaService.update(veterinaria);
		return new ResponseEntity<>(veterinariaActualizada, HttpStatus.OK);
	}

	// DELETE: Eliminar veterinaria
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteVeterinaria(@PathVariable Integer id) {
		veterinariaService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// GET: Búsqueda por nombre
	@GetMapping("/search/nombre")
	public ResponseEntity<List<Veterinaria>> searchByNombre(@RequestParam String nombre) {
		List<Veterinaria> veterinarias = veterinariaService.findByNombreContainingIgnoreCase(nombre);
		return new ResponseEntity<>(veterinarias, HttpStatus.OK);
	}

	// GET: Búsqueda por ciudad
	@GetMapping("/search/ciudad")
	public ResponseEntity<List<Veterinaria>> searchByCiudad(@RequestParam String ciudad) {
		List<Veterinaria> veterinarias = veterinariaService.findByCiudad(ciudad);
		return new ResponseEntity<>(veterinarias, HttpStatus.OK);
	}

	// GET: Verificar si existe por nombre
	@GetMapping("/exists/nombre")
	public ResponseEntity<Boolean> existsByNombre(@RequestParam String nombre) {
		boolean exists = veterinariaService.existsByNombre(nombre);
		return new ResponseEntity<>(exists, HttpStatus.OK);
	}

	// GET: Búsqueda múltiple
	@GetMapping("/search")
	public ResponseEntity<List<Veterinaria>> search(@RequestParam String keyword) {
		List<Veterinaria> resultados = veterinariaService.searchByMultipleFields(keyword);
		return new ResponseEntity<>(resultados, HttpStatus.OK);
	}

	// GET: Contar total de veterinarias
	@GetMapping("/count")
	public ResponseEntity<Long> countVeterinarias() {
		long count = veterinariaService.countTotalVeterinarias();
		return new ResponseEntity<>(count, HttpStatus.OK);
	}

	// GET: Ordenar por nombre ASC
	@GetMapping("/ordenadas/nombre")
	public ResponseEntity<List<Veterinaria>> getOrdenadasPorNombre() {
		List<Veterinaria> veterinarias = veterinariaService.findAllOrderByNombreAsc();
		return new ResponseEntity<>(veterinarias, HttpStatus.OK);
	}
}
