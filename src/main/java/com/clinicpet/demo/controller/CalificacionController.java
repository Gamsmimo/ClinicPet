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

import com.clinicpet.demo.model.Calificacion;
import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.service.ICalificacionService;
import com.clinicpet.demo.service.IPerfilVeterinarioService;

@Controller
@RequestMapping("/calificaciones")
public class CalificacionController {

	private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CalificacionController.class);

	@Autowired
	private ICalificacionService calificacionService;

	@Autowired
	private IPerfilVeterinarioService veterinarioService;

	// USUARIO: mostrar formulario de calificación
	@GetMapping("/usuario")
	public String mostrarFormularioCalificacion(Model model) {
		List<PerfilVeterinario> veterinarios = veterinarioService.obtenerTodosLosPerfilesVeterinario();
		model.addAttribute("veterinarios", veterinarios);
		model.addAttribute("calificacion", new Calificacion());
		return "Calificacion/calificacion"; // tu HTML del carrusel
	}

	// USUARIO: guardar calificación
	@PostMapping("/save")
	public String guardarCalificacion(@ModelAttribute("calificacion") Calificacion calificacion) {
		calificacionService.guardarCalificacion(calificacion);
		LOGGER.info("Nueva calificación guardada para veterinario ID: {}", calificacion.getVeterinario().getId());
		return "redirect:/calificaciones/usuario";
	}

	// VETERINARIO: ver solo sus calificaciones
	@GetMapping("/veterinario/{idVeterinario}")
	public String listarCalificacionesVeterinario(@PathVariable("idVeterinario") Integer idVeterinario, Model model) {
		List<Calificacion> calificaciones = calificacionService.BuscarPorVeterinario(idVeterinario);
		model.addAttribute("calificaciones", calificaciones);
		return "Calificacion/veterinario";
	}

	// ADMINISTRADOR: listar todas las calificaciones
	@GetMapping("/admin")
	public String listarCalificacionesAdmin(Model model) {
		List<Calificacion> calificaciones = calificacionService.listarCalificaciones();
		model.addAttribute("calificaciones", calificaciones);
		return "Calificacion/admin";
	}

	// ADMINISTRADOR: editar calificación
	@GetMapping("/edit/{id}")
	public String editarCalificacion(@PathVariable("id") Integer id, Model model) {
		Optional<Calificacion> calificacion = calificacionService.buscarPorId(id);
		if (calificacion.isPresent()) {
			model.addAttribute("calificacion", calificacion.get());
			return "Calificacion/editCalificacion";
		} else {
			LOGGER.warn("Calificación no encontrada ID: {}", id);
			return "redirect:/calificaciones/admin";
		}
	}

	// ADMINISTRADOR: actualizar calificación
	@PostMapping("/update")
	public String actualizarCalificacion(@ModelAttribute("calificacion") Calificacion calificacion) {
		calificacionService.guardarCalificacion(calificacion);
		LOGGER.info("Calificación actualizada ID: {}", calificacion.getId());
		return "redirect:/calificaciones/admin";
	}

	// ADMINISTRADOR: eliminar calificación
	@GetMapping("/delete/{id}")
	public String eliminarCalificacion(@PathVariable("id") Integer id) {
		calificacionService.eliminarCalificacion(id);
		LOGGER.info("Calificación eliminada ID: {}", id);
		return "redirect:/calificaciones/admin";
	}
}