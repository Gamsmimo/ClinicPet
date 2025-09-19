package com.clinicpet.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinicpet.demo.model.Carrito;
import com.clinicpet.demo.service.ICarritoService;

@RestController
@RequestMapping("/api/carritos")
@CrossOrigin(origins = "*")
public class CarritoController {

	@Autowired
	private ICarritoService carritoService;

	@GetMapping
	public List<Carrito> listarCarritos() {
		return carritoService.listarCarritos();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Carrito> obtenerCarritoPorId(@PathVariable Integer id) {
		Optional<Carrito> carrito = carritoService.obtenerCarritoPorId(id);
		return carrito.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/usuario/{usuarioId}/activo")
	public ResponseEntity<Carrito> obtenerCarritoActivoUsuario(@PathVariable Integer usuarioId) {
		Optional<Carrito> carrito = carritoService.obtenerCarritoActivoUsuario(usuarioId);
		return carrito.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/usuario/{usuarioId}/crear")
	public Carrito crearCarritoParaUsuario(@PathVariable Integer usuarioId) {
		return carritoService.crearCarritoParaUsuario(usuarioId);
	}

	@PostMapping("/usuario/{usuarioId}/confirmar")
	public Carrito confirmarCarrito(@PathVariable Integer usuarioId) {
		return carritoService.confirmarCarrito(usuarioId);
	}

	@PostMapping("/usuario/{usuarioId}/cancelar")
	public ResponseEntity<Void> cancelarCarrito(@PathVariable Integer usuarioId) {
		carritoService.cancelarCarrito(usuarioId);
		return ResponseEntity.ok().build();
	}

}