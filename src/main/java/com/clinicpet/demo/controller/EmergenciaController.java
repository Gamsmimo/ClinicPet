package com.clinicpet.demo.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinicpet.demo.model.Emergencia;
import com.clinicpet.demo.service.IEmergenciaService;

@RestController
@RequestMapping("/api/emergencias")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen
public class EmergenciaController {

	@Autowired
	private IEmergenciaService emergenciaService;

	// CREATE - Crear una nueva emergencia
	@PostMapping
	public ResponseEntity<?> crearEmergencia(@RequestBody Emergencia emergencia) {
		try {
			Emergencia nuevaEmergencia = emergenciaService.guardarEmergencia(emergencia);
			return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEmergencia);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	// Para cargar el formulario 
	@GetMapping("/form-data")
	public ResponseEntity<Map<String, Object>> getFormData() {
		// devolver datos necesarios para el formulario como lista de mascotas, tipos de emergencia, etc.
		Map<String, Object> formData = new HashMap<>();
		formData.put("tiposEmergencia", List.of("accidente", "enfermedad", "intoxicacion", "parto", "cirugia"));
		// ... más datos si es necesario

		return ResponseEntity.ok(formData);
	}

	// Obtener todas las emergencias
	@GetMapping
	public ResponseEntity<List<Emergencia>> obtenerTodasEmergencias() {
		List<Emergencia> emergencias = emergenciaService.obtenerTodasLasEmergencias();
		return ResponseEntity.ok(emergencias);
	}

	// Obtener emergencia por ID
	@GetMapping("/{id}")
	public ResponseEntity<Emergencia> obtenerEmergenciaPorId(@PathVariable Integer id) {
		Optional<Emergencia> emergencia = emergenciaService.obtenerEmergenciaPorId(id);
		return emergencia.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Actualizar una emergencia existente
	@PutMapping("/{id}")
	public ResponseEntity<Emergencia> actualizarEmergencia(@PathVariable Integer id,
			@RequestBody Emergencia emergencia) {
		try {
			// Verificar que la emergencia existe
			if (!emergenciaService.obtenerEmergenciaPorId(id).isPresent()) {
				return ResponseEntity.notFound().build();
			}

			emergencia.setId(id); // Asegurar que se actualice la emergencia correcta
			Emergencia emergenciaActualizada = emergenciaService.guardarEmergencia(emergencia);
			return ResponseEntity.ok(emergenciaActualizada);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	// Eliminar una emergencia
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarEmergencia(@PathVariable Integer id) {
		try {
			emergenciaService.eliminarEmergencia(id);
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Obtener emergencias por tipo
	@GetMapping("/tipo/{tipo}")
	public ResponseEntity<List<Emergencia>> obtenerEmergenciasPorTipo(@PathVariable String tipo) {
		List<Emergencia> emergencias = emergenciaService.obtenerEmergenciasPorTipo(tipo);
		return ResponseEntity.ok(emergencias);
	}

	// Obtener emergencias por mascota
	@GetMapping("/mascota/{mascotaId}")
	public ResponseEntity<List<Emergencia>> obtenerEmergenciasPorMascota(@PathVariable Integer mascotaId) {
		List<Emergencia> emergencias = emergenciaService.obtenerEmergenciasPorMascota(mascotaId);
		return ResponseEntity.ok(emergencias);
	}

	// Obtener emergencias por veterinario
	@GetMapping("/veterinario/{veterinarioId}")
	public ResponseEntity<List<Emergencia>> obtenerEmergenciasPorVeterinario(@PathVariable Integer veterinarioId) {
		List<Emergencia> emergencias = emergenciaService.obtenerEmergenciasPorVeterinario(veterinarioId);
		return ResponseEntity.ok(emergencias);
	}

	// Obtener emergencias por mascota y rango de fechas
	@GetMapping("/mascota/{mascotaId}/rango-fechas")
	public ResponseEntity<List<Emergencia>> obtenerEmergenciasPorRangoFechas(@PathVariable Integer mascotaId,
			@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin) {

		List<Emergencia> emergencias = emergenciaService.obtenerEmergenciasPorMascotaYRangoFechas(mascotaId, inicio,
				fin);
		return ResponseEntity.ok(emergencias);
	}

	// Obtener emergencias recientes (últimas 24 horas)
	@GetMapping("/recientes")
	public ResponseEntity<List<Emergencia>> obtenerEmergenciasRecientes() {
		List<Emergencia> emergencias = emergenciaService.obtenerEmergenciasRecientes();
		return ResponseEntity.ok(emergencias);
	}

	// Obtener emergencias de hoy
	@GetMapping("/hoy")
	public ResponseEntity<List<Emergencia>> obtenerEmergenciasDeHoy() {
		List<Emergencia> emergencias = emergenciaService.obtenerEmergenciasDeHoy();
		return ResponseEntity.ok(emergencias);
	}

	// Obtener emergencias por rango de fechas general
	@GetMapping("/rango-fechas")
	public ResponseEntity<List<Emergencia>> obtenerEmergenciasPorRangoFechasGeneral(@RequestParam LocalDateTime inicio,
			@RequestParam LocalDateTime fin) {

		List<Emergencia> emergencias = emergenciaService.obtenerEmergenciasPorRangoFechas(inicio, fin);
		return ResponseEntity.ok(emergencias);
	}
}
