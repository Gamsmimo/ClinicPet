package com.clinicpet.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.clinicpet.demo.model.Cita;
import com.clinicpet.demo.model.Emergencia;
import com.clinicpet.demo.model.Inventario;
import com.clinicpet.demo.model.Mascota;
import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.model.Producto;
import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.model.Veterinaria;
import com.clinicpet.demo.service.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/perfil-veterinario")
public class PerfilVeterinarioController {

	@Autowired
	private IPerfilVeterinarioService perfilVeterinarioService;

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IMascotaService mascotaService;

	@Autowired
	private ICitaService citaService;

	@Autowired
	private HttpSession session;

	@Autowired
	private IInventarioService inventarioService;

	@Autowired
	private IVeterinariaService veterinariaService;

	@Autowired
	private IEmergenciaService emergenciaService;

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


			// 🔹 Aquí cargamos TODAS las mascotas de la BD
			List<Mascota> mascotas = mascotaService.listarMascotas();
			System.out.println("🐾 Mascotas encontradas: " + mascotas.size());
			mascotas.forEach(m -> System.out.println(" - " + m.getId() + " | " + m.getNombre()));
			model.addAttribute("mascotas", mascotas);
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

		// 🔹 Aquí cargamos TODAS las mascotas de la BD
		List<Mascota> mascotas = mascotaService.listarMascotas();
		System.out.println("🐾 Mascotas encontradas: " + mascotas.size());
		mascotas.forEach(m -> System.out.println(" - " + m.getId() + " | " + m.getNombre()));
		model.addAttribute("mascotas", mascotas);

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

	@GetMapping("/adopciones")
	public String adopciones() {
		return "perfil-veterinario/adopciones";
	}

	@GetMapping("/pet-shop")
	public String petShop() {
		return "perfil-veterinario/pet-shop";
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

	// MODAL CITA!!!!!!!!!!!!!
	@GetMapping("/perfil-veterinario")
	public String nuevaCita(Model model) {

		List<Mascota> mascotas = mascotaService.listarMascotas();
		model.addAttribute("mascotas", mascotas);
		return "perfil-veterinario/perfil-veterinario"; // tu vista que tiene el modal
	}

	@PostMapping("/cita/guardar")
	public String guardarCita(@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
			@RequestParam("hora") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
			@RequestParam("motivo") String motivo, @RequestParam("mascota.id") Integer mascotaId) {

		Cita cita = new Cita();
		cita.setFechaHora(LocalDateTime.of(fecha, hora));
		cita.setMotivo(motivo);

		// Asignar la mascota
		Mascota mascota = mascotaService.buscarMascotaPorId(mascotaId)
				.orElseThrow(() -> new IllegalArgumentException("Mascota no encontrada con id: " + mascotaId));

		cita.setMascota(mascota);

		// Asignar el veterinario logueado
		PerfilVeterinario vet = obtenerVeterinarioLogueado(); // Método que devuelve el vet logueado
		cita.setVeterinario(vet);

		citaService.guardarCita(cita);

		return "perfil-veterinario/perfil-veterinario";
	}

	private PerfilVeterinario obtenerVeterinarioLogueado() {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuario == null) {
			throw new IllegalStateException("No hay usuario logueado");
		}
		return perfilVeterinarioService.buscarPorUsuarioId(usuario.getId())
				.orElseThrow(() -> new IllegalArgumentException("Perfil veterinario no encontrado"));
	}

	// MODAL DE AGREGAR PRODUCTO!!!!!!!!!!!!!!1
	@PostMapping("/producto/guardar")
	public String guardarProducto(@ModelAttribute Producto producto, @RequestParam("fileImagen") MultipartFile imagen,
			@RequestParam("cantidadDisponible") Integer cantidadDisponible,
			@RequestParam("idveterinaria") Integer idVeterinaria, Model model) {

		if (!imagen.isEmpty()) {
			try {
				String nombreArchivo = Paths.get(imagen.getOriginalFilename()).getFileName().toString();
				String rutaBase = "C:/imagenesProductos/";
				Path ruta = Paths.get(rutaBase + nombreArchivo);
				Files.createDirectories(ruta.getParent());
				imagen.transferTo(ruta.toFile());
				producto.setImagen(nombreArchivo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		productoService.crearProducto(producto); // Guarda en la base de datos
		List<Producto> productos = productoService.obtenerTodosLosProductos();
		model.addAttribute("productos", productos);

		Inventario inventario = inventarioService.obtenerInventarioPorVeterinariaYProducto(idVeterinaria,
				producto.getId());

		if (inventario != null) {
			inventario.agregarStock(cantidadDisponible); // suma si ya existeInteger veterinariaId, Integer productoId
		} else {
			inventario = new Inventario();
			inventario.setProducto(producto);
			Veterinaria veterinaria = veterinariaService.obtenerPorId(idVeterinaria)
					.orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));

			inventario.setVeterinaria(veterinaria);
			inventario.setCantidadDisponible(cantidadDisponible);
			inventario.actualizarEstado();
		}

		inventarioService.guardarInventario(inventario);

		return "redirect:/perfil-veterinario";
	}

	// MODAL EMERGENCIA!!!!!!!!!!!!!!!!
	@PostMapping("/emergencia/guardar")
	public String guardarEmergencia(@RequestParam("mascotaId") Integer mascotaId,
			@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
			@RequestParam("hora") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
			@RequestParam("tipo") String tipo, @RequestParam("descripcion") String descripcion,
			@RequestParam("veterinariaId") Integer veterinariaId, @RequestParam("veterinarioId") Integer veterinarioId,
			RedirectAttributes redirectAttributes) {

		Mascota mascota = mascotaService.buscarMascotaPorId(mascotaId)
				.orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

		Veterinaria veterinaria = veterinariaService.obtenerPorId(veterinariaId)
				.orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));

		PerfilVeterinario veterinario = perfilVeterinarioService.buscarPorId(veterinarioId)
				.orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

		Emergencia emergencia = new Emergencia();
		emergencia.setMascota(mascota);
		emergencia.setVeterinaria(veterinaria);
		emergencia.setVeterinario(veterinario);
		emergencia.setTipo(tipo);
		emergencia.setDescripcion(descripcion);
		emergencia.setFechayhora(LocalDateTime.of(fecha, hora));

		emergenciaService.guardarEmergencia(emergencia);

		redirectAttributes.addFlashAttribute("mensaje", "Emergencia registrada con éxito");
		return "redirect:/perfil-veterinario";
	}

}