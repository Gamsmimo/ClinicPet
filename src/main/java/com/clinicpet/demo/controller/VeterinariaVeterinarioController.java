package com.clinicpet.demo.controller;

import com.clinicpet.demo.model.VeterinariaVeterinario;
import com.clinicpet.demo.service.IVeterinariaVeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relaciones/veterinaria-veterinario")
public class VeterinariaVeterinarioController {

	@Autowired
	private IVeterinariaVeterinarioService relacionService;

	// CRUD básico
	@GetMapping
	public List<VeterinariaVeterinario> getAllRelaciones() {
		return relacionService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<VeterinariaVeterinario> getRelacionById(@PathVariable Integer id) {
		return relacionService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public VeterinariaVeterinario createRelacion(@RequestBody VeterinariaVeterinario relacion) {
		return relacionService.save(relacion);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteRelacion(@PathVariable Integer id) {
		relacionService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	// Endpoints específicos
	@GetMapping("/veterinaria/{veterinariaId}/veterinarios")
	public ResponseEntity<?> getVeterinariosPorVeterinaria(@PathVariable Integer veterinariaId) {
		return ResponseEntity.ok(relacionService.findVeterinariosByVeterinariaId(veterinariaId));
	}

	@GetMapping("/veterinario/{veterinarioId}/veterinarias")
	public ResponseEntity<?> getVeterinariasPorVeterinario(@PathVariable Integer veterinarioId) {
		return ResponseEntity.ok(relacionService.findVeterinariasByVeterinarioId(veterinarioId));
	}

	@PostMapping("/veterinaria/{veterinariaId}/veterinario/{veterinarioId}")
	public ResponseEntity<VeterinariaVeterinario> crearRelacion(@PathVariable Integer veterinariaId,
			@PathVariable Integer veterinarioId) {
		return ResponseEntity.ok(relacionService.createRelationship(veterinariaId, veterinarioId));
	}

	@DeleteMapping("/veterinaria/{veterinariaId}/veterinario/{veterinarioId}")
	public ResponseEntity<?> eliminarRelacion(@PathVariable Integer veterinariaId,
			@PathVariable Integer veterinarioId) {
		relacionService.removeRelationship(veterinariaId, veterinarioId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/veterinaria/{veterinariaId}/existe/veterinario/{veterinarioId}")
	public ResponseEntity<Boolean> existeRelacion(@PathVariable Integer veterinariaId,
			@PathVariable Integer veterinarioId) {
		return ResponseEntity.ok(relacionService.existsByVeterinariaIdAndVeterinarioId(veterinariaId, veterinarioId));
	}
}
