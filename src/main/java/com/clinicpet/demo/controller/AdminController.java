package com.clinicpet.demo.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clinicpet.demo.model.Veterinaria;
import com.clinicpet.demo.service.IVeterinariaService;

@Controller
@RequestMapping("/admin") // ruta del navegador
public class AdminController {

	private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private IVeterinariaService veterinariaService;

	@GetMapping("/home")
	public String adminHome() {
		return "Admin/admin";
	}

	// Gestion de la veterinaria (aprobar veterinarias y ver qveterinarias y
	// veterinarios activos)
	@GetMapping("/gestion-veterinaria")
	public String gestionVeterinaria(Model model) {
		model.addAttribute("veterinariasPendientes", veterinariaService.listarPorEstado("Pendiente"));
		model.addAttribute("veterinariasAprobadas", veterinariaService.listarPorEstado("Aprobada"));
		  model.addAttribute("veterinariasDesactivadas", veterinariaService.listarPorEstado("Inactiva"));
		return "Admin/gestionveterinaria";
	}

	// solicitudes
	@GetMapping("/solicitudes")
	public String solicitudes(Model model) {
		model.addAttribute("veterinariasPendientes", veterinariaService.listarPorEstado("Pendiente"));
		return "Admin/gestionveterinaria\"";
	}

	// Mostrar detalle de veterinaria
	@GetMapping("/{id}")
	public String detalleVeterinaria(@PathVariable Integer id, Model model) {
		Veterinaria veterinaria = veterinariaService.findById(id);
		model.addAttribute("veterinaria", veterinaria);
		return "Admin/gestionveterinaria";
	}

	// Aprobar veterinaria
	@PostMapping("/aprobar/{id}")
	public String aprobar(@PathVariable Integer id) {
		veterinariaService.aprobarVeterinaria(id);
		return "redirect:/admin/gestion-veterinaria";
	}

	// Rechazar veterinaria
	@PostMapping("/rechazar/{id}")
	public String rechazar(@PathVariable Integer id) {
		veterinariaService.rechazarVeterinaria(id);
		return "redirect:/admin/gestion-veterinaria";
	}

	@PostMapping("/desactivar/{id}")
	public String desactivar(@PathVariable Integer id) {
		veterinariaService.desactivarVeterinaria(id);
		return "redirect:/admin/gestion-veterinaria";
	}

	@PostMapping("/activar/{id}")
	public String activar(@PathVariable Integer id) {
	    veterinariaService.activarVeterinaria(id);
	    return "redirect:/admin/gestion-veterinaria";
	}
	
	// Listar veterinarias
	@GetMapping("/lista")
	public String listar(Model model) {
		model.addAttribute("veterinarias", veterinariaService.findAll());
		return "Admin/gestionveterinaria";
	}
}