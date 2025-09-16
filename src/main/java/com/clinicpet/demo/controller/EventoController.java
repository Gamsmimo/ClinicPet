package com.clinicpet.demo.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;

import com.clinicpet.demo.model.Evento;
import com.clinicpet.demo.service.IEventoService;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*")

public class EventoController {

	@Autowired
	private IEventoService eventoService;

	@PostMapping
	public ResponseEntity<?> crearEvento(@RequestBody Evento evento) {
		try {
			Evento nuevoEvento = eventoService.guardarEvento(evento);
			return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEvento);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@GetMapping
	public ResponseEntity<List<Evento>> obtenerTodosEventos() {
		List<Evento> eventos = eventoService.obtenerTodosLosEventos();
		return ResponseEntity.ok(eventos);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizarEvento(@PathVariable Integer id, @RequestBody Evento evento) {
		try {
			evento.setId(id);
			Evento eventoActualizado = eventoService.guardarEvento(evento);
			return ResponseEntity.ok(eventoActualizado);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarEvento(@PathVariable Integer id) {
		try {
			eventoService.eliminarEvento(id);
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/home")
	public ResponseEntity<List<Evento>> obtenerEventosParaHome() {
		List<Evento> eventos = eventoService.obtenerEventosVigentes();
		return ResponseEntity.ok(eventos);
	}
}
