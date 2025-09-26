package com.clinicpet.demo.controller;

import com.clinicpet.demo.model.Mascota;
import com.clinicpet.demo.model.PerfilAdmin;
import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.model.ReporteMaltrato;
import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.model.Veterinaria;
import com.clinicpet.demo.repository.IMascotaRepository;
import com.clinicpet.demo.repository.IPerfilVeterinarioRepository;
import com.clinicpet.demo.repository.IReporteDeMaltratoRepository;
import com.clinicpet.demo.repository.IUsuarioRepository;
import com.clinicpet.demo.repository.IVeterinariaRepository;
import com.clinicpet.demo.service.IPerfilAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private IPerfilAdminService adminService;

	@Autowired
	private IUsuarioRepository usuarioRepo;

	@Autowired
	private IMascotaRepository mascotaRepo;

	@Autowired
	private IPerfilVeterinarioRepository veterinarioRepo;

	@Autowired
	private IVeterinariaRepository veterinariaRepo;

	@Autowired
	private IReporteDeMaltratoRepository reporteRepo;

	@GetMapping
	public String mostrarPanelAdmin(Model model) {
		try {
			System.out.println("✅ Accediendo a /admin");

			PerfilAdmin admin = obtenerAdminLogueado();
			model.addAttribute("admin", admin);

			// Cargar datos para cada sección
			cargarDatosDashboard(model);

			System.out.println("✅ Panel admin cargado correctamente");
			return "admin/panelAdmin";

		} catch (Exception e) {
			System.out.println("❌ Error en mostrarPanelAdmin: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Error al cargar el panel de administración");
			return "admin/panelAdmin";
		}
	}

	@GetMapping("/usuarios")
	public String gestionUsuarios(Model model) {
		PerfilAdmin admin = obtenerAdminLogueado();
		model.addAttribute("admin", admin);

		// Cargar todos los usuarios
		List<Usuario> usuarios = usuarioRepo.findAll();
		model.addAttribute("usuarios", usuarios);

		return "admin/panelAdmin";
	}

	@GetMapping("/mascotas")
	public String gestionMascotas(Model model) {
		PerfilAdmin admin = obtenerAdminLogueado();
		model.addAttribute("admin", admin);

		// Cargar todas las mascotas
		List<Mascota> mascotas = mascotaRepo.findAll();
		model.addAttribute("mascotas", mascotas);

		return "admin/panelAdmin";
	}

	@GetMapping("/veterinaria")
	public String gestionVeterinaria(Model model) {
		PerfilAdmin admin = obtenerAdminLogueado();
		model.addAttribute("admin", admin);

		// Cargar veterinarios y veterinarias
		List<PerfilVeterinario> veterinarios = veterinarioRepo.findAll();
		List<Veterinaria> veterinarias = veterinariaRepo.findAll();

		model.addAttribute("veterinarios", veterinarios);
		model.addAttribute("veterinarias", veterinarias);

		return "admin/panelAdmin";
	}

	@GetMapping("/reportes")
	public String gestionReportes(Model model) {
		PerfilAdmin admin = obtenerAdminLogueado();
		model.addAttribute("admin", admin);

		// Cargar reportes de maltrato
		List<ReporteMaltrato> reportes = reporteRepo.findAll();
		model.addAttribute("reportes", reportes);

		return "admin/panelAdmin";
	}

	// actualizar prefil
	@PostMapping("/perfil/editar")
	public String guardarPerfil(@RequestParam String nombres, @RequestParam String apellidos,
			@RequestParam String correo, @RequestParam String telefono, RedirectAttributes redirectAttributes) {
		try {
			Optional<PerfilAdmin> adminOptional = adminService.buscarPorId(1);

			if (adminOptional.isPresent()) {
				PerfilAdmin adminActual = adminOptional.get();
				adminActual.setNombres(nombres);
				adminActual.setApellidos(apellidos);
				adminActual.setCorreo(correo);
				adminActual.setTelefono(telefono);

				adminService.guardar(adminActual);
				redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado correctamente");
			} else {
				redirectAttributes.addFlashAttribute("error", "Administrador no encontrado");
			}

		} catch (Exception e) {
			System.out.println("❌ Error al guardar perfil: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Error al actualizar el perfil");
		}
		return "redirect:/admin#profile";
	}

	@PostMapping("/perfil/imagen")
	public String subirimagen(@RequestParam("imagen") MultipartFile archivoimagen,
			RedirectAttributes redirectAttributes) {
		try {
			if (archivoimagen.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Por favor seleccione una imagen");
				return "redirect:/admin#profile";
			}

			// Validar tipo de archivo
			String contentType = archivoimagen.getContentType();
			if (contentType == null || !contentType.startsWith("image/")) {
				redirectAttributes.addFlashAttribute("error",
						"Por favor seleccione un archivo de imagen válido (JPG, PNG, GIF)");
				return "redirect:/admin#profile";
			}

			// Convertir imagen a Base64
			byte[] bytesImagen = archivoimagen.getBytes();
			String imagenBase64 = Base64.getEncoder().encodeToString(bytesImagen);
			String imagenDataURL = "data:" + contentType + ";base64," + imagenBase64;

			// Actualizar el admin
			Optional<PerfilAdmin> adminOptional = adminService.buscarPorId(1);

			if (adminOptional.isPresent()) {
				PerfilAdmin admin = adminOptional.get();
				admin.setImagen(imagenDataURL);
				adminService.guardar(admin);
				redirectAttributes.addFlashAttribute("mensaje", "imagen actualizada correctamente");
			} else {
				redirectAttributes.addFlashAttribute("error", "Administrador no encontrado");
			}

		} catch (IOException e) {
			System.out.println("❌ Error al procesar la imagen: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Error al procesar la imagen");
		} catch (Exception e) {
			System.out.println("❌ Error al subir imagen: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Error al subir la imagen");
		}

		return "redirect:/admin#profile";
	}

	// registrar veterinaria
	@PostMapping("/veterinarias/registrar")
	public String registrarVeterinaria(@RequestParam String nombre, @RequestParam String direccion,
			@RequestParam String telefono, @RequestParam String correo, @RequestParam String horario,
			RedirectAttributes redirectAttributes) {
		try {
			Veterinaria veterinaria = new Veterinaria();
			veterinaria.setNombre(nombre);
			veterinaria.setDireccion(direccion);
			veterinaria.setTelefono(telefono);
			veterinaria.setCorreo(correo);
			veterinaria.setHorario(horario);
			veterinaria.setEstado("Activa");

			veterinariaRepo.save(veterinaria);
			redirectAttributes.addFlashAttribute("mensaje", "Veterinaria registrada correctamente");

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al registrar veterinaria");
		}
		return "redirect:/admin#veterinaria";
	}

	// registrar veterinario
	@PostMapping("/veterinarios/registrar")
	public String registrarVeterinario(@RequestParam String nombres, @RequestParam String apellidos,
			@RequestParam String correo, @RequestParam String telefono, @RequestParam String especialidad,
			@RequestParam String tarjetaProfesional, RedirectAttributes redirectAttributes) {
		try {
			Usuario usuario = new Usuario();
			usuario.setNombres(nombres);
			usuario.setApellidos(apellidos);
			usuario.setCorreo(correo);
			usuario.setTelefono(telefono);
			usuario.setActivo(true);

			// guardar usuario
			Usuario usuarioGuardado = usuarioRepo.save(usuario);

			// crear perfil veterinario
			PerfilVeterinario veterinario = new PerfilVeterinario();
			veterinario.setUsuario(usuarioGuardado);
			veterinario.setEspecialidad(especialidad);
			veterinario.setTarjetaProfesional(tarjetaProfesional);
			veterinario.setEstado(true);
			veterinario.setExperiencia("Recién registrado");

			veterinarioRepo.save(veterinario);
			redirectAttributes.addFlashAttribute("mensaje", "Veterinario registrado correctamente");

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al registrar veterinario");
		}
		return "redirect:/admin#veterinaria";
	}

	// activar y desactivar usuario
	@PostMapping("/usuarios/{accion}/{usuarioId}")
	public String gestionarUsuario(@PathVariable String accion, @PathVariable Integer usuarioId,
			RedirectAttributes redirectAttributes) {
		try {
			Optional<Usuario> usuarioOpt = usuarioRepo.findById(usuarioId);

			if (usuarioOpt.isPresent()) {
				Usuario usuario = usuarioOpt.get();

				if ("activar".equals(accion)) {
					usuario.setActivo(true);
					redirectAttributes.addFlashAttribute("mensaje", "Usuario activado correctamente");
				} else if ("desactivar".equals(accion)) {
					usuario.setActivo(false);
					redirectAttributes.addFlashAttribute("mensaje", "Usuario desactivado correctamente");
				}

				usuarioRepo.save(usuario);
			} else {
				redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error en la operación");
		}
		return "redirect:/admin/usuarios";
	}

	// reportes
	@PostMapping("/reportes/asignar/{reporteId}")
	public String asignarReporte(@PathVariable Integer reporteId, @RequestParam String autoridad,
			RedirectAttributes redirectAttributes) {
		try {
			Optional<ReporteMaltrato> reporteOpt = reporteRepo.findById(reporteId);

			if (reporteOpt.isPresent()) {
				ReporteMaltrato reporte = reporteOpt.get();
				reporte.setEstado("Asignado a " + autoridad);
				reporteRepo.save(reporte);

				redirectAttributes.addFlashAttribute("mensaje", "Reporte asignado a " + autoridad);
			} else {
				redirectAttributes.addFlashAttribute("error", "Reporte no encontrado");
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al asignar reporte");
		}
		return "redirect:/admin/reportes";
	}

	// ver detalles
	@GetMapping("/usuario/{id}")
	@ResponseBody
	public Usuario obtenerUsuario(@PathVariable Integer id) {
		return usuarioRepo.findById(id).orElse(null);
	}

	@GetMapping("/veterinario/{id}")
	@ResponseBody
	public PerfilVeterinario obtenerVeterinario(@PathVariable Integer id) {
		return veterinarioRepo.findById(id).orElse(null);
	}

	@GetMapping("/mascota/{id}")
	@ResponseBody
	public Mascota obtenerMascota(@PathVariable Integer id) {
		return mascotaRepo.findById(id).orElse(null);
	}

	private PerfilAdmin obtenerAdminLogueado() {
		try {
			Optional<PerfilAdmin> adminOptional = adminService.buscarPorId(1);

			if (adminOptional.isPresent()) {
				return adminOptional.get();
			} else {
				// Crear admin por defecto
				PerfilAdmin admin = new PerfilAdmin();
				admin.setNombres("Administrador");
				admin.setApellidos("Principal");
				admin.setCorreo("admin@clinicpet.com");
				admin.setTelefono("+57 300 123 4567");
				admin.setCedula("123456789");
				admin.setImagen("https://via.placeholder.com/150");

				return adminService.guardar(admin);
			}
		} catch (Exception e) {
			System.out.println("❌ Error al obtener admin: " + e.getMessage());
			PerfilAdmin admin = new PerfilAdmin();
			admin.setNombres("Administrador");
			admin.setApellidos("Principal");
			return admin;
		}
	}

	private void cargarDatosDashboard(Model model) {
		model.addAttribute("totalUsuarios", usuarioRepo.count());
		model.addAttribute("totalMascotas", mascotaRepo.count());
		model.addAttribute("totalVeterinarias", veterinariaRepo.count());
		model.addAttribute("totalReportes", reporteRepo.count());
	}
}
