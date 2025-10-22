package com.clinicpet.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import com.clinicpet.demo.repository.IUsuarioRepository;
import com.clinicpet.demo.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

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

	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Autowired
	private IUsuarioService usuarioService;

	// ==================== VISTA PRINCIPAL ====================
	@GetMapping
	public String mostrarPerfilVeterinario(HttpSession session, Model model) {
		System.out.println("🔍 Accediendo a vista principal del veterinario");

		// ✅ USAR SESIÓN
		Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuarioLogueado == null) {
			System.out.println("❌ Usuario no autenticado - Redirigiendo al login");
			return "redirect:/usuarios/iniciarsesion";
		}

		// Verificar si es veterinario
		if (usuarioLogueado.getRol().getId() != 2) {
			System.out.println("❌ Usuario no tiene rol de veterinario");
			return "redirect:/acceso-denegado";
		}

		String correo = usuarioLogueado.getCorreo();
		System.out.println("📧 Buscando perfil para: " + correo);

		// Buscar perfil veterinario
		Optional<PerfilVeterinario> perfilOpt = perfilVeterinarioService.buscarPorUsuarioId(usuarioLogueado.getId());

		if (perfilOpt.isPresent()) {
			model.addAttribute("perfilVeterinario", perfilOpt.get());
			System.out.println("✅ Perfil veterinario encontrado para: " + correo);
		} else {
			System.out.println("❌ ERROR: Veterinario sin perfil en BD: " + correo);
			model.addAttribute("error", "Error: Perfil de veterinario no encontrado.");
		}

		// 🔹 Cargar mascotas (MANTENIDO)
		List<Mascota> mascotas = mascotaService.listarMascotas();
		System.out.println("🐾 Mascotas encontradas: " + mascotas.size());
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

	// ==================== SECCION CONFIGURACION ====================

	@PostMapping("/configuracion/actualizar")
	public String actualizarConfiguracion(@ModelAttribute PerfilVeterinario perfilForm,
			@RequestParam(value = "foto", required = false) MultipartFile fotoFile, HttpSession session,
			RedirectAttributes redirectAttributes) {
		System.out.println("🔄 Procesando actualización de configuración");

		// ✅ USAR SESIÓN
		Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuarioLogueado == null) {
			System.out.println("❌ Usuario no autenticado - Redirigiendo al login");
			return "redirect:/usuarios/iniciarsesion";
		}

		try {
			// Verificar si es veterinario
			if (usuarioLogueado.getRol().getId() != 2) {
				redirectAttributes.addFlashAttribute("error", "❌ No tiene permisos de veterinario");
				return "redirect:/perfil-veterinario";
			}

			// Buscar perfil veterinario existente
			Optional<PerfilVeterinario> perfilExistenteOpt = perfilVeterinarioService
					.buscarPorUsuarioId(usuarioLogueado.getId());

			if (perfilExistenteOpt.isPresent()) {
				PerfilVeterinario perfilExistente = perfilExistenteOpt.get();

				// ✅ ACTUALIZAR USUARIO EXISTENTE
				Usuario usuarioActual = usuarioLogueado;
				usuarioActual.setNombres(perfilForm.getUsuario().getNombres());
				usuarioActual.setApellidos(perfilForm.getUsuario().getApellidos());
				usuarioActual.setTelefono(perfilForm.getUsuario().getTelefono());
				usuarioActual.setDireccion(perfilForm.getUsuario().getDireccion());

				// 🔥 **MANEJO DE FOTO - CORREGIDO**
				if (fotoFile != null && !fotoFile.isEmpty()) {
					// Validaciones de foto
					if (fotoFile.getSize() > 2 * 1024 * 1024) {
						redirectAttributes.addFlashAttribute("error", "La imagen no debe superar 2MB");
						return "redirect:/perfil-veterinario";
					}

					String contentType = fotoFile.getContentType();
					if (contentType == null
							|| (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
						redirectAttributes.addFlashAttribute("error", "Formato no válido. Solo JPG y PNG");
						return "redirect:/perfil-veterinario";
					}

					// Usar sistema uploads
					String uploadDir = System.getProperty("user.dir") + "/uploads/";
					String extension = contentType.equals("image/jpeg") ? ".jpg" : ".png";
					String fileName = "vet_" + usuarioLogueado.getId() + extension;

					Path uploadPath = Paths.get(uploadDir);
					if (!Files.exists(uploadPath)) {
						Files.createDirectories(uploadPath);
					}

					// Guardar foto
					Path filePath = uploadPath.resolve(fileName);
					Files.copy(fotoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

					// 🔥 **IMPORTANTE: Actualizar la imagen en el usuario**
					usuarioActual.setImagen("/uploads/" + fileName);
				}

				Usuario usuarioGuardado = usuarioService.actualizarUsuario(usuarioActual.getId(), usuarioActual);

				// ✅ ACTUALIZAR PERFIL EXISTENTE
				perfilExistente.setEspecialidad(perfilForm.getEspecialidad());
				perfilExistente.setExperiencia(perfilForm.getExperiencia());
				perfilExistente.setTarjetaProfesional(perfilForm.getTarjetaProfesional());

				PerfilVeterinario perfilGuardado = perfilVeterinarioService.actualizarPerfil(perfilExistente.getId(),
						perfilExistente);

				// ✅ ACTUALIZAR SESIÓN con los nuevos datos
				session.setAttribute("usuarioLogueado", usuarioGuardado);

				redirectAttributes.addFlashAttribute("success", "✅ Perfil actualizado correctamente");
				System.out.println("✅ Perfil actualizado para: " + usuarioLogueado.getCorreo());
			} else {
				System.out
						.println("❌ ERROR: Intentando actualizar perfil que no existe: " + usuarioLogueado.getCorreo());
				redirectAttributes.addFlashAttribute("error", "❌ Error: Perfil no encontrado");
			}

			return "redirect:/perfil-veterinario";

		} catch (Exception e) {
			System.out.println("❌ Error al actualizar perfil: " + e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "❌ Error al actualizar perfil: " + e.getMessage());
			return "redirect:/perfil-veterinario";
		}
	}

	@PostMapping("/change-password")
	@Transactional
	public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
			@RequestParam String confirmPassword, HttpSession session, Model model) {

		System.out.println("🎯 changePassword ejecutado!");

		try {
			// Obtener el usuario actual de la base de datos
			// Buscamos usuarios activos que sean veterinarios (rol_id = 2 según tus datos)
			List<Usuario> veterinarios = usuarioRepository.findByRolId(2);

			if (veterinarios.isEmpty()) {
				System.out.println("❌ No hay veterinarios en el sistema");
				model.addAttribute("error", "No se encontró usuario veterinario");
				return "perfil-veterinario";
			}

			// Tomar el primer veterinario activo
			Usuario usuario = veterinarios.get(0);
			System.out.println("👤 Veterinario encontrado: " + usuario.getCorreo());

			// ... resto del código de validación igual
			if (!usuario.getPassword().equals(currentPassword)) {
				model.addAttribute("error", "La contraseña actual es incorrecta");
				return "perfil-veterinario";
			}

			if (!newPassword.equals(confirmPassword)) {
				model.addAttribute("error", "Las nuevas contraseñas no coinciden");
				return "perfil-veterinario";
			}

			usuarioService.actualizarPassword(usuario.getId(), newPassword);
			session.invalidate();

			model.addAttribute("success", "Contraseña actualizada correctamente. Por favor inicie sesión nuevamente.");
			return "redirect:/usuarios/iniciarsesion";

		} catch (Exception e) {
			model.addAttribute("error", "Error: " + e.getMessage());
			return "perfil-veterinario";
		}
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