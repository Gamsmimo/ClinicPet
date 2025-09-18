package com.clinicpet.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.clinicpet.demo.model.PerfilAdmin;
import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.service.IPerfilAdminService;
import com.clinicpet.demo.service.IPerfilVeterinarioService;
import com.clinicpet.demo.service.IVeterinariaService;

@Controller
@RequestMapping("/admin") // ruta del navegador
public class AdminController {

	private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private IVeterinariaService veterinariaService;

	@Autowired
	private IPerfilVeterinarioService veterinarioService;

	@Autowired
	private IPerfilAdminService adminService;

//Vista principal del admin
	@GetMapping("/home")
	public String adminHome() {
		return "Admin/admin";
	}

	// Gestion de la veterinaria (aprobar veterinarias y ver q veterinarias estan
	// activas o inactivas)

	// VETERINARIAS
	@GetMapping("/gestion-veterinaria")
	public String gestionVeterinaria(Model model) {
		model.addAttribute("veterinariasPendientes", veterinariaService.listarPorEstado("Pendiente"));
		model.addAttribute("veterinariasAprobadas", veterinariaService.listarPorEstado("Aprobada"));
		model.addAttribute("veterinariasDesactivadas", veterinariaService.listarPorEstado("Inactiva"));

		// VETERINARIOS
		List<PerfilVeterinario> Aprobada = veterinarioService.ListarPorEstado("Aprobada");
		List<PerfilVeterinario> Pendiente = veterinarioService.ListarPorEstado("Pendiente");
		List<PerfilVeterinario> Inactiva = veterinarioService.ListarPorEstado("Inactiva");

		model.addAttribute("veterinariosAprobados", Aprobada);
		model.addAttribute("veterinariosPendientes", Pendiente);
		model.addAttribute("veterinariosInactivos", Inactiva);

		return "Admin/gestionveterinaria";
	}

	// solicitudes de veterinarias para ser aprobadas
	@GetMapping("/solicitudes")
	public String solicitudes(Model model) {
		model.addAttribute("veterinariasPendientes", veterinariaService.listarPorEstado("Pendiente"));
		return "Admin/gestionveterinaria";
	}

	// Aprobar veterinarias
	@PostMapping("/aprobar/{id}")
	public String aprobar(@PathVariable Integer id) {
		veterinariaService.aprobarVeterinaria(id);
		return "redirect:/admin/gestion-veterinaria";
	}

	// Rechazar veterinarias
	@PostMapping("/rechazar/{id}")
	public String rechazar(@PathVariable Integer id) {
		veterinariaService.rechazarVeterinaria(id);
		return "redirect:/admin/gestion-veterinaria";
	}

	// desactivar una veterinaria despues de ser aprobada y q este activa
	@PostMapping("/desactivar/{id}")
	public String desactivar(@PathVariable Integer id) {
		veterinariaService.desactivarVeterinaria(id);
		return "redirect:/admin/gestion-veterinaria";
	}

	// Activar una vez que este aprobada e inactiva
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

	// VETERINARIOS

	// Aprobar veterinario
	@PostMapping("/aprobarVeterinario/{id}")
	public String aprobarVeterinario(@PathVariable Integer id) {
		veterinarioService.aprobarVeterinario(id);
		return "redirect:/admin/gestion-veterinaria";
	}

	// Rechazar veterinario
	@PostMapping("/rechazarVeterinario/{id}")
	public String rechazarVeterianrio(@PathVariable Integer id) {
		veterinarioService.rechazarVeterinario(id);
		return "redirect:/admin/gestion-veterinaria";

	}

	// desactivar veterinario
	@PostMapping("/desactivarVeterinario/{id}")
	public String desactivarVeterinario(@PathVariable Integer id) {
		veterinarioService.desactivarVeterinario(id);
		return "redirect:/admin/gestion-veterinaria";
	}

	// Activar veterinario
	@PostMapping("/activarVeterinario/{id}")
	public String activarVeterinario(@PathVariable Integer id) {
		veterinarioService.activarVeterinario(id);
		return "redirect:/admin/gestion-veterinaria";
	}

	// Editar veterinario
	// @PostMapping("/editarVeterinario/{id}")
	// public String editarVeterinario(@PathVariable Integer id) {
	// veterinarioService.editarVeterinario(id);
	// return "Admin/gestionveterinaria";
	// }

	// Guardar veterinario
	@PostMapping("/guardar")
	public String guardarVeterinario(@ModelAttribute PerfilVeterinario veterinario) {
		veterinarioService.crearPerfilVeterinario(veterinario);
		return "redirect:/admin/gestion-veterinaria";

	}

	// actualizar veterinario
	@PostMapping("/actualizar/{id}")
	public String actualizarPerfilVeterinario(@PathVariable Integer id,
			@ModelAttribute PerfilVeterinario perfilActualizado) {
		veterinarioService.actualizarPerfilVeterinario(id, perfilActualizado);
		return "redirect:/admin/gestion-veterinaria";

	}

	// CALIFICACION

	// MASCOTAS Y ADOPCIONES
	@GetMapping("/mascotas-adopciones")
	public String mascotasAdopciones(Model model) {
		return "Admin/mascotasadopciones";
	}

	// GESTION USUARIO
	@GetMapping("/gestion-usuario")
	public String gestionUsuario(Model model) {
		return "Admin/gestionusuarios";
	}

	// COMENTARIOS
	@GetMapping("/comentarios")
	public String comentarios(Model model) {
		return "Admin/comentarios";
	}

	// PERFIL ADMIN
	@GetMapping("/perfil")
	public String perfil(Model model) {
		PerfilAdmin admin = adminService.obtenerAdminPrincipal().orElse(new PerfilAdmin()); // fallback si no existe
		model.addAttribute("admin", admin);
		return "Admin/perfil";
	}

	// Actualizar datos
	@PostMapping("/perfil/actualizar-datos")
	public String actualizarPerfil(@ModelAttribute PerfilAdmin adminActualizado) {// Forzamos que sea siempre id = 1
		adminActualizado.setId(1);
		adminService.actualizarAdmin(adminActualizado);
		return "redirect:/admin/perfil";
	}

	// Actualizar foto
	@PostMapping("/perfil/actualizar-foto")
	public String actualizarFoto(@RequestParam("foto") MultipartFile foto) { // Mandamos directo al id fijo = 1
		adminService.actualizarFoto(1, foto);
		return "redirect:/admin/perfil";
	}

	// REPORTES
	@GetMapping("/reportes-maltrato")
	public String reportesmaltrato(Model model) {
		return "Admin/reportes";
	}

	// RESPALDO
	@GetMapping("/respaldo")
	public String respaldo(Model model) {
		return "Admin/respaldo";
	}
}
