package com.clinicpet.demo.controller;

import com.clinicpet.demo.model.Mascota;
import com.clinicpet.demo.model.PerfilAdmin;
import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.model.Rol;
import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.model.Veterinaria;
import com.clinicpet.demo.repository.IMascotaRepository;
import com.clinicpet.demo.repository.IPerfilVeterinarioRepository;
import com.clinicpet.demo.repository.IReporteDeMaltratoRepository;
import com.clinicpet.demo.repository.IRolRepository;
import com.clinicpet.demo.repository.IUsuarioRepository;
import com.clinicpet.demo.repository.IVeterinariaRepository;
import com.clinicpet.demo.service.HistoriaClinicaServiceImplement;
import com.clinicpet.demo.service.IPerfilAdminService;
import com.clinicpet.demo.service.VeterinariaServiceImplement;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class PerfilAdminController {

	private final VeterinariaServiceImplement veterinariaServiceImplement;

	private final HistoriaClinicaServiceImplement historiaClinicaServiceImplement;

	@Autowired
	private IPerfilAdminService adminService;

	@Autowired
	private IUsuarioRepository usuarioRepo;

	@Autowired
	private IMascotaRepository mascotaRepo;

	@Autowired
	private IRolRepository rolRepository;

	@Autowired
	private IPerfilVeterinarioRepository veterinarioRepo;

	@Autowired
	private IVeterinariaRepository veterinariaRepo;

	@Autowired
	private IReporteDeMaltratoRepository reporteRepo;

	PerfilAdminController(HistoriaClinicaServiceImplement historiaClinicaServiceImplement,
			VeterinariaServiceImplement veterinariaServiceImplement) {
		this.historiaClinicaServiceImplement = historiaClinicaServiceImplement;
		this.veterinariaServiceImplement = veterinariaServiceImplement;
	}

	@GetMapping
	public String mostrarPanelAdmin(Model model, HttpSession session) {
		try {
			System.out.println("✅ Accediendo a /admin - Cargando datos desde BD");

			PerfilAdmin admin = obtenerAdminLogueado(session);
			model.addAttribute("admin", admin);

			// CARGAR TODOS LOS DATOS ACTUALIZADOS DESDE BD
			cargarDatosDashboard(model);
			cargarUsuariosParaVista(model);
			cargarVeterinariasParaVista(model);
			cargarMascotasParaVista(model);

			System.out.println("✅ Datos cargados correctamente desde BD");
			return "admin/panelAdmin";

		} catch (Exception e) {
			System.out.println("❌ Error en mostrarPanelAdmin: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Error al cargar el panel de administración");
			return "redirect:/usuarios/iniciarsesion";
		}
	}

	// MÉTODO MEJORADO PARA REGISTRAR VETERINARIOS
	@PostMapping("/veterinarios/registrar")
	public String registrarVeterinario(@RequestParam String nombres, @RequestParam String apellidos,
			@RequestParam Integer edad, @RequestParam String correo, @RequestParam String telefono,
			@RequestParam String especialidad, @RequestParam String tarjetaProfesional,
			@RequestParam String experiencia, @RequestParam String password, @RequestParam String tipoDocumento,
			@RequestParam String numDocumento, @RequestParam String direccion, HttpSession session,
			RedirectAttributes redirectAttributes1) {
		if (!verificarSesionAdmin(session)) {
			return "redirect:/usuarios/iniciarsesion";
		}

		try {
			System.out.println("🔍 REGISTRANDO VETERINARIO EN BD: " + nombres + " " + apellidos);

			// ✅ Verificar si el correo ya existe
			if (usuarioRepo.existsByCorreo(correo)) {
				redirectAttributes1.addFlashAttribute("error", "El correo ya está registrado");
				return "redirect:/admin#users";
			}

			// ✅ Buscar rol VETERINARIO (ID 2)
			Optional<Rol> rolOpt = rolRepository.findById(2);
			if (rolOpt.isEmpty()) {
				redirectAttributes1.addFlashAttribute("error", "Rol VETERINARIO no encontrado");
				return "redirect:/admin#users";
			}

			// ✅ Crear usuario veterinario CON TODOS LOS CAMPOS OBLIGATORIOS
			Usuario usuario = new Usuario();
			usuario.setNombres(nombres);
			usuario.setApellidos(apellidos);
			usuario.setCorreo(correo);
			usuario.setTelefono(telefono);
			usuario.setPassword(password);
			usuario.setEdad(edad);
			usuario.setTipoDocumento(tipoDocumento);
			usuario.setNumDocumento(numDocumento);
			usuario.setDireccion(direccion);
			usuario.setActivo(true);
			usuario.setRol(rolOpt.get());

			// Guardar usuario en BD
			Usuario usuarioGuardado = usuarioRepo.save(usuario);
			System.out.println("✅ USUARIO GUARDADO EN BD CON ID: " + usuarioGuardado.getId());

			// Crear perfil veterinario
			PerfilVeterinario veterinario = new PerfilVeterinario();
			veterinario.setUsuario(usuarioGuardado);
			veterinario.setEspecialidad(especialidad);
			veterinario.setTarjetaProfesional(tarjetaProfesional);
			veterinario.setExperiencia(experiencia);
			veterinario.setEstado(true);

			veterinarioRepo.save(veterinario);
			System.out.println("✅ VETERINARIO GUARDADO EN BD: " + nombres + " " + apellidos);

			redirectAttributes1.addFlashAttribute("mensaje", "Veterinario registrado correctamente");

		} catch (Exception e) {
			System.out.println("❌ ERROR AL REGISTRAR VETERINARIO EN BD: " + e.getMessage());
			e.printStackTrace();
			redirectAttributes1.addFlashAttribute("error", "Error al registrar veterinario: " + e.getMessage());
		}

		return "redirect:/admin#users";
	}

	// MÉTODO PARA REGISTRAR VETERINARIAS
	@PostMapping("/veterinarias/registrar")
	public String registrarVeterinaria(@RequestParam String nombre, @RequestParam String rut,
			@RequestParam String direccion, @RequestParam String telefono, @RequestParam String correo,
			@RequestParam String horario, @RequestParam String descripcion, @RequestParam String estado,
			HttpSession session, RedirectAttributes redirectAttributes) {
		if (!verificarSesionAdmin(session)) {
			return "redirect:/usuarios/iniciarsesion";
		}
		try {
			System.out.println("🔍 REGISTRANDO VETERINARIA EN BD: " + nombre);

			// ✅ Crear veterinaria
			Veterinaria veterinaria = new Veterinaria();
			veterinaria.setNombre(nombre);
			veterinaria.setRut(rut);
			veterinaria.setDireccion(direccion);
			veterinaria.setTelefono(telefono);
			veterinaria.setCorreo(correo);
			veterinaria.setHorario(horario);
			veterinaria.setDescripcion(descripcion);
			veterinaria.setEstado(estado);

			// guardar en BD
			veterinariaRepo.save(veterinaria);

			System.out.println("✅ VETERINARIA GUARDADA EN BD: " + nombre);
			redirectAttributes.addFlashAttribute("mensaje", "Veterinaria registrada correctamente");

		} catch (Exception e) {
			System.out.println("❌ ERROR AL REGISTRAR VETERINARIA EN BD: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Error al registrar veterinaria: " + e.getMessage());
		}
		return "redirect:/admin#vets";
	}

	// MÉTODO PARA ACTUALIZAR PERFIL ADMIN
	@PostMapping("/perfil/editar")
	public String guardarPerfil(@RequestParam String nombres, @RequestParam String apellidos,
			@RequestParam String correo, @RequestParam String telefono, HttpSession session,
			RedirectAttributes redirectAttributes) {
		if (!verificarSesionAdmin(session)) {
			return "redirect:/usuarios/iniciarsesion";
		}
		try {
			Optional<PerfilAdmin> adminOptional = adminService.buscarPorId(1);

			if (adminOptional.isPresent()) {
				PerfilAdmin adminActual = adminOptional.get();
				adminActual.setNombres(nombres);
				adminActual.setApellidos(apellidos);
				adminActual.setCorreo(correo);
				adminActual.setTelefono(telefono);

				// ✅ Guardar en BD
				adminService.guardar(adminActual);

				System.out.println("✅ PERFIL ADMIN ACTUALIZADO EN BD");
				redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado correctamente");
			} else {
				redirectAttributes.addFlashAttribute("error", "Administrador no encontrado");
			}

		} catch (Exception e) {
			System.out.println("❌ Error al guardar perfil en BD: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Error al actualizar el perfil");
		}
		return "redirect:/admin#profile";
	}

	// ✅ MÉTODO PARA SUBIR IMAGEN
	@PostMapping("/perfil/imagen")
	public String subirImagen(@RequestParam("imagen") MultipartFile archivoImagen, HttpSession session,
			RedirectAttributes redirectAttributes) {
		if (!verificarSesionAdmin(session)) {
			return "redirect:/usuarios/iniciarsesion";
		}
		try {
			if (archivoImagen.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Archivo vacío");
				return "redirect:/admin#profile";
			}

			String contentType = archivoImagen.getContentType();
			if (!contentType.equals("image/JPG") && !contentType.equals("image/png")
					&& !contentType.equals("image/jpg")) {
				redirectAttributes.addFlashAttribute("error", "Solo se permiten imágenes JPG, JPEG o PNG");
				return "redirect:/admin#profile";
			}

			byte[] bytes = archivoImagen.getBytes();
			String base64 = Base64.getEncoder().encodeToString(bytes);

			Optional<PerfilAdmin> adminOpt = adminService.buscarPorId(1);
			if (adminOpt.isPresent()) {
				PerfilAdmin admin = adminOpt.get();
				admin.setImagen("data:" + contentType + ";base64," + base64);
				adminService.guardar(admin);
				redirectAttributes.addFlashAttribute("mensaje", "Imagen actualizada correctamente");
			} else {
				redirectAttributes.addFlashAttribute("error", "Admin no encontrado");
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al subir imagen");
			e.printStackTrace();
		}

		return "redirect:/admin#profile";
	}

	// MÉTODO PARA CARGAR DATOS DESDE LA BASE DE DATOS
	private void cargarUsuariosParaVista(Model model) {
		List<Usuario> usuarios = usuarioRepo.findAll().stream()
				.filter(u -> !"admin@clinicpet.com".equalsIgnoreCase(u.getCorreo())).toList();
		model.addAttribute("usuarios", usuarios);
		System.out.println("✅ " + usuarios.size() + " usuarios cargados desde BD");
	}

	private void cargarVeterinariasParaVista(Model model) {
		List<Veterinaria> veterinarias = veterinariaRepo.findAll();
		model.addAttribute("veterinarias", veterinarias);
		System.out.println("✅ " + veterinarias.size() + " veterinarias cargadas desde BD");
	}

	private void cargarVeterinariosParaVista(Model model) {
		List<PerfilVeterinario> veterinarios = veterinarioRepo.findAll();
		model.addAttribute("veterinarios", veterinarios);
		System.out.println("✅ " + veterinarios.size() + " veterinarios cargados desde BD");
	}

	private void cargarDatosDashboard(Model model) {
		model.addAttribute("totalUsuarios", usuarioRepo.count());
		model.addAttribute("totalMascotas", mascotaRepo.count());
		model.addAttribute("totalVeterinarias", veterinariaRepo.count());
		model.addAttribute("totalReportes", reporteRepo.count());

		System.out.println("📊 Dashboard - Usuarios: " + usuarioRepo.count() + ", Mascotas: " + mascotaRepo.count()
				+ ", Veterinarias: " + veterinariaRepo.count());
	}

	private PerfilAdmin obtenerAdminLogueado(HttpSession session) {
		try {
			// Obtener usuario de sesión
			Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

			if (usuarioLogueado == null || usuarioLogueado.getRol().getId() != 3) {
				throw new RuntimeException("Usuario no autorizado");
			}

			// Buscar perfil admin por usuario_id
			Optional<PerfilAdmin> adminOpt = adminService.buscarPorUsuarioId(usuarioLogueado.getId());

			if (adminOpt.isPresent()) {
				return adminOpt.get();
			} else {
				throw new RuntimeException("Perfil admin no encontrado");
			}

		} catch (Exception e) {
			System.out.println("❌ Error al obtener admin: " + e.getMessage());
			throw new RuntimeException("Error de autenticación");
		}
	}

	// Lista para la vista gestión de Mascotas
	@GetMapping("/mascotas")
	public String verGestionMascotas(Model model) {
		cargarDatosDashboard(model); // por si quieres los contadores
		model.addAttribute("mascotas", mascotaRepo.findAll());
		return "admin/panelAdmin :: #mascotas"; // fragmento Thymeleaf
	}

	private void cargarMascotasParaVista(Model model) {
		List<Mascota> mascotas = mascotaRepo.findAll();
		model.addAttribute("mascotas", mascotas);
		System.out.println("mascotas cargadas: " + mascotas.size());
	}

	// metodo para boton ver detalles
	// usuario
	@GetMapping("/usuario/{id}")
	@ResponseBody
	public Usuario detalleUsuario(@PathVariable Integer id) {
		return usuarioRepo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	}

	// veterinario
	@GetMapping("/veterinario/{id}")
	@ResponseBody
	public PerfilVeterinario detalleVeterinario(@PathVariable Integer id) {
		return veterinarioRepo.findById(id).orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
	}

	// veterinaria
	@GetMapping("/veterinaria/{id}")
	@ResponseBody
	public Veterinaria detalleVeterinaria(@PathVariable Integer id) {
		return veterinariaRepo.findById(id).orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));
	}

	// metodo para activar y desactivar
	@PostMapping("/usuario/cambiar-estado/{id}")
	public String cambiarEstadoUsuario(@PathVariable Integer id, @RequestParam boolean activo,
			RedirectAttributes redirectAttributes) {
		Optional<Usuario> opt = usuarioRepo.findById(id);
		if (opt.isPresent()) {
			Usuario u = opt.get();
			u.setActivo(activo);
			usuarioRepo.save(u);
			redirectAttributes.addFlashAttribute("mensaje", "Usuario " + (activo ? "desactivado" : "activado"));
		} else {
			redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
		}
		return "redirect:/admin#users";
	}

	@PostMapping("/veterinaria/cambiar-estado/{id}")
	public String cambiarEstadoVeterinaria(@PathVariable Integer id, @RequestParam boolean estado,
			RedirectAttributes redirectAttributes) {
		Optional<Veterinaria> opt = veterinariaRepo.findById(id);
		if (opt.isPresent()) {
			Veterinaria v = opt.get();
			v.setEstado(estado ? "Activa" : "Inactiva");
			veterinariaRepo.save(v);
			redirectAttributes.addFlashAttribute("mensaje", "Veterinaria " + (estado ? "activada" : "desactivada"));
		} else {
			redirectAttributes.addFlashAttribute("error", "Veterinaria no encontrada");
		}
		return "redirect:/admin#vets";
	}

	// de lo de login
	private boolean verificarSesionAdmin(HttpSession session) {
		Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
		return usuarioLogueado != null && usuarioLogueado.getRol().getId() == 3;
	}

	// CERRAR SESIÓN
	@GetMapping("/logout")
	public String cerrarSesion(HttpSession session, RedirectAttributes redirectAttributes) {
		session.invalidate(); // Destruye la sesión
		redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada correctamente");
		return "redirect:/"; // Redirige al index principal
	}

}