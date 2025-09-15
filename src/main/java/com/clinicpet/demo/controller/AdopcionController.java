package com.clinicpet.demo.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clinicpet.demo.model.Adopcion;
import com.clinicpet.demo.model.Mascota;
import com.clinicpet.demo.model.PerfilUsuario;
import com.clinicpet.demo.service.IAdopcionService;
import com.clinicpet.demo.service.IMascotaService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/adopciones")
public class AdopcionController {
	// instancia logger
	private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdopcionController.class);

	@Autowired
	private IAdopcionService adopcionService;

	@Autowired
	private IMascotaService mascotaService;

	// Listar adopciones para los usuarios
	@GetMapping("/usuario")
	public String listarusuario(Model model) {
		List<Adopcion> disponibles = adopcionService.buscarAdopcionesByEstado("DISPONIBLE");
		model.addAttribute("disponibles", disponibles);
		model.addAttribute("adopcion", new Adopcion());
		LOGGER.info("Listado de mascotas disponibles: {}", disponibles.size());
		return "Adopcion/adopcion";
	}

	// Listar adopciones para el veterinario
	@GetMapping("/veterinario")
	public String listarVeterinario(Model model) {
		List<Adopcion> disponibles = adopcionService.buscarAdopcionesByEstado("Disponible");
		List<Adopcion> adoptadas = adopcionService.buscarAdopcionesByEstado("Adoptado");

		model.addAttribute("disponibles", disponibles);
		model.addAttribute("adoptadas", adoptadas);

		LOGGER.info("Mascotas disponibles: {}, adoptadas: {}", disponibles.size(), adoptadas.size());
		return "adopcion/veterinario"; // vista para veterinarios
	}

	// Crear una adopción
	@GetMapping("/create")
	public String createAdopcion(Model model) {
		model.addAttribute("adopcion", new Adopcion());
		LOGGER.info("Entrando al formulario de creación de adopción");
		return "adopcion/adopcion";
	}

	// Guardar una adopción
	@PostMapping("/save")
	public String guardarAdopcion(@ModelAttribute("adopcion") Adopcion adopcion, HttpSession session) {

		// obtener el usuario actual
		PerfilUsuario usuarioActual = (PerfilUsuario) session.getAttribute("usuario");
		if (usuarioActual == null) {
			return "redirect:/iniciasesion";
		}

		// 1. Crear la mascota y asociarla al usuario que publica
		Mascota mascota = new Mascota();
		mascota.setNombre(adopcion.getMascota().getNombre());
		mascota.setEspecie(adopcion.getMascota().getEspecie());
		mascota.setRaza(adopcion.getMascota().getRaza());
		mascota.setEdad(adopcion.getMascota().getEdad());
		mascota.setGenero(adopcion.getMascota().getGenero());
		mascota.setTamaño(adopcion.getMascota().getTamaño());
		mascota.setDescripcion(adopcion.getMascota().getDescripcion());
		mascota.setEstado("Disponible");
		mascota.setFoto(adopcion.getMascota().getFoto());
		mascota.setPerfilusuario(usuarioActual); // Aquí se asigna el usuario que publica

		// Guardar la mascota
		mascota = mascotaService.guardarMascota(mascota);

		// Crear la adopción asociando la mascota
		adopcion.setMascota(mascota);
		adopcion.setEstado("Disponible");

		// Guardar la adopción
		adopcionService.guardarAdopcion(adopcion);

		return "redirect:/adopciones/usuario";
	}

	// VETERINARIO
	// Editar una adopción
	@GetMapping("/edit/{id}")
	public String editAdopcion(@PathVariable("id") Integer id, Model model) {
		Optional<Adopcion> adopcion = adopcionService.buscarAdopcionById(id);

		if (adopcion.isPresent()) {
			LOGGER.info("Editando adopción con ID: {}", id);
			model.addAttribute("adopcion", adopcion.get());
			return "adopcion/editAdopcion";
		} else {
			LOGGER.warn("No se encontró adopción con ID: {}", id);
			return "redirect:/adopciones/veterinario";
		}
	}

	// Actualizar una adopción
	@PostMapping("/update")
	public String updateAdopcion(@ModelAttribute("adopcion") Adopcion adopcion) {
		LOGGER.info("Actualizando adopción ID: {}", adopcion.getId());
		adopcionService.guardarAdopcion(adopcion);
		return "redirect:/adopciones/veterinario";
	}

	// Eliminar una adopción
	@GetMapping("/delete/{id}")
	public String deleteAdopcion(@PathVariable("id") Integer id) {
		LOGGER.warn("Eliminando adopción con ID: {}", id);
		adopcionService.eliminarAdopcion(id);
		return "redirect:/adopciones/veterinario";
	}
}
