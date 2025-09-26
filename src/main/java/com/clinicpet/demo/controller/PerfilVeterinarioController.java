package com.clinicpet.demo.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.service.IPerfilVeterinarioService;

@Controller
@RequestMapping("/perfil-veterinario")
public class PerfilVeterinarioController {

	@Autowired
	private IPerfilVeterinarioService perfilVeterinarioService;

	// ==================== VISTA PRINCIPAL ====================
	@GetMapping
	public String mostrarPerfilVeterinario(Principal principal, Model model) {
		System.out.println("🔍 Accediendo a vista principal del veterinario");

		// ✅ MODIFICADO: Si no hay usuario, mostrar datos de prueba en lugar de
		// redirigir
		if (principal == null) {
			System.out.println("⚠️ Usuario no autenticado - Mostrando datos de prueba");
			PerfilVeterinario perfilPrueba = crearDatosPrueba();
			model.addAttribute("perfilVeterinario", perfilPrueba);
			model.addAttribute("modoPrueba", true); // Para mostrar indicador en la vista
			return "perfil-veterinario/perfil-veterinario";
		}

		String correo = principal.getName();
		Optional<PerfilVeterinario> perfilOpt = perfilVeterinarioService.buscarPorUsuarioCorreo(correo);

		if (perfilOpt.isPresent()) {
			model.addAttribute("perfilVeterinario", perfilOpt.get());
			System.out.println("✅ Perfil encontrado para: " + correo);
		} else {
			// Crear perfil básico si no existe
			PerfilVeterinario perfilNuevo = crearPerfilBasico(correo);
			PerfilVeterinario perfilGuardado = perfilVeterinarioService.guardarPerfil(perfilNuevo);
			model.addAttribute("perfilVeterinario", perfilGuardado);
			System.out.println("🆕 Perfil creado para: " + correo);
		}

		return "perfil-veterinario/perfil-veterinario";
	}

	// ==================== OTRAS SECCIONES (PLACEHOLDERS) ====================
	@GetMapping("/inicio")
	public String inicio() {
		return "perfil-veterinario/inicio";
	}

	@GetMapping("/agenda")
	public String agenda(Model model) {
		return "perfil-veterinario/agenda";
	}

	@GetMapping("/historias-clinicas")
	public String historiasClinicas() {
		return "perfil-veterinario/historias-clinicas";
	}

	@GetMapping("/tratamientos")
	public String tratamientos() {
		return "perfil-veterinario/tratamientos";
	}

	@GetMapping("/adopciones")
	public String adopciones() {
		return "perfil-veterinario/adopciones";
	}

	@GetMapping("/pet-shop")
	public String petShop() {
		return "perfil-veterinario/pet-shop";
	}

	@GetMapping("/reportes")
	public String reportes() {
		return "perfil-veterinario/reportes";
	}

	// ==================== SECCIÓN CONFIGURACIÓN ====================
	@GetMapping("/configuracion")
	public String mostrarConfiguracion(Principal principal, Model model) {
		System.out.println("🔧 Accediendo a configuración");

		// ✅ MODIFICADO: Si no hay usuario, permitir acceso con datos de prueba
		if (principal == null) {
			System.out.println("⚠️ Usuario no autenticado - Modo prueba para configuración");
			PerfilVeterinario perfilPrueba = crearDatosPrueba();
			model.addAttribute("perfilVeterinario", perfilPrueba);
			model.addAttribute("modoPrueba", true);
			return "perfil-veterinario/configuracion";
		}

		String correo = principal.getName();
		Optional<PerfilVeterinario> perfilOpt = perfilVeterinarioService.buscarPorUsuarioCorreo(correo);

		if (perfilOpt.isPresent()) {
			model.addAttribute("perfilVeterinario", perfilOpt.get());
			System.out.println("✅ Cargando configuración para: " + correo);
		} else {
			PerfilVeterinario perfilTemporal = crearPerfilBasico(correo);
			model.addAttribute("perfilVeterinario", perfilTemporal);
			System.out.println("📝 Mostrando formulario para nuevo perfil");
		}

		return "perfil-veterinario/configuracion";
	}

	// ==================== ACTUALIZAR CONFIGURACIÓN ====================
	@PostMapping("/configuracion/actualizar")
	public String actualizarConfiguracion(@ModelAttribute PerfilVeterinario perfilForm, Principal principal,
			RedirectAttributes redirectAttributes) {
		System.out.println("🔄 Procesando actualización de configuración");

		// ✅ MODIFICADO: Si no hay usuario, mostrar mensaje pero no redirigir
		if (principal == null) {
			redirectAttributes.addFlashAttribute("error",
					"⚠️ Modo prueba: Los cambios no se guardarán. Inicie sesión para guardar permanentemente.");
			return "redirect:/perfil-veterinario/configuracion";
		}

		try {
			String correo = principal.getName();
			Optional<PerfilVeterinario> perfilExistenteOpt = perfilVeterinarioService.buscarPorUsuarioCorreo(correo);

			if (perfilExistenteOpt.isPresent()) {
				PerfilVeterinario perfilActualizado = perfilVeterinarioService
						.actualizarPerfil(perfilExistenteOpt.get().getId(), perfilForm);

				redirectAttributes.addFlashAttribute("success", "✅ Perfil actualizado correctamente");
				System.out.println("✅ Perfil actualizado para: " + correo);
			} else {
				perfilForm.getUsuario().setCorreo(correo);
				PerfilVeterinario perfilNuevo = perfilVeterinarioService.guardarPerfil(perfilForm);

				redirectAttributes.addFlashAttribute("success", "✅ Perfil creado correctamente");
				System.out.println("✅ Nuevo perfil creado para: " + correo);
			}

			return "redirect:/perfil-veterinario/configuracion";

		} catch (Exception e) {
			System.out.println("❌ Error al actualizar perfil: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "❌ Error al actualizar perfil: " + e.getMessage());
			return "redirect:/perfil-veterinario/configuracion";
		}
	}

	// ==================== MÉTODOS AUXILIARES ====================
	private PerfilVeterinario crearPerfilBasico(String correo) {
		PerfilVeterinario perfil = new PerfilVeterinario();
		Usuario usuario = new Usuario();

		usuario.setCorreo(correo);
		usuario.setNombres("Veterinario");
		usuario.setTelefono("Sin teléfono registrado");

		perfil.setUsuario(usuario);
		perfil.setEspecialidad("Especialidad no definida");
		perfil.setExperiencia("Experiencia no especificada");

		return perfil;
	}

	// ✅ NUEVO: Método para datos de prueba cuando no hay usuario autenticado
	private PerfilVeterinario crearDatosPrueba() {
		PerfilVeterinario perfil = new PerfilVeterinario();
		Usuario usuario = new Usuario();

		usuario.setNombres("Dr. Juan Pérez (Modo Prueba)");
		usuario.setCorreo("prueba@clinicpet.com");
		usuario.setTelefono("+1 555-0000");

		perfil.setUsuario(usuario);
		perfil.setEspecialidad("Cirugía Veterinaria");
		perfil.setExperiencia("5 años de experiencia en cirugía de pequeños animales");

		return perfil;
	}

}