package com.clinicpet.demo.controller;

import com.clinicpet.demo.model.*;
import com.clinicpet.demo.repository.*;
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
public class PerfilAdminController {

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

	@GetMapping
	public String mostrarPanelAdmin(Model model) {
		try {
			System.out.println("✅ Accediendo a /admin - Cargando datos desde BD");

			PerfilAdmin admin = obtenerAdminLogueado();
			model.addAttribute("admin", admin);

			// ✅ CARGAR TODOS LOS DATOS ACTUALIZADOS DESDE BD
			cargarDatosDashboard(model);
			cargarUsuariosParaVista(model);
			cargarVeterinariasParaVista(model);
			cargarVeterinariosParaVista(model);

			System.out.println("✅ Datos cargados correctamente desde BD");
			return "admin/panelAdmin";

		} catch (Exception e) {
			System.out.println("❌ Error en mostrarPanelAdmin: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Error al cargar el panel de administración");
			return "admin/panelAdmin";
		}
	}

	// ✅ MÉTODO MEJORADO PARA REGISTRAR VETERINARIOS
	@PostMapping("/veterinarios/registrar")
	public String registrarVeterinario(@RequestParam String nombres, @RequestParam String apellidos,
			@RequestParam Integer edad, @RequestParam String correo, @RequestParam String telefono,
			@RequestParam String especialidad, @RequestParam String tarjetaProfesional,
			@RequestParam String experiencia, @RequestParam String password, @RequestParam String tipoDocumento,
			@RequestParam String numDocumento, @RequestParam String direccion, RedirectAttributes redirectAttributes) {

		try {
			System.out.println("🔍 REGISTRANDO VETERINARIO EN BD: " + nombres + " " + apellidos);

			// ✅ Verificar si el correo ya existe
			if (usuarioRepo.existsByCorreo(correo)) {
				redirectAttributes.addFlashAttribute("error", "El correo ya está registrado");
				return "redirect:/admin#users";
			}

			// ✅ Buscar rol VETERINARIO (ID 2)
			Optional<Rol> rolOpt = rolRepository.findById(2);
			if (rolOpt.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Rol VETERINARIO no encontrado");
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

			// ✅ Guardar usuario en BD
			Usuario usuarioGuardado = usuarioRepo.save(usuario);
			System.out.println("✅ USUARIO GUARDADO EN BD CON ID: " + usuarioGuardado.getId());

			// ✅ Crear perfil veterinario
			PerfilVeterinario veterinario = new PerfilVeterinario();
			veterinario.setUsuario(usuarioGuardado);
			veterinario.setEspecialidad(especialidad);
			veterinario.setTarjetaProfesional(tarjetaProfesional);
			veterinario.setExperiencia(experiencia);
			veterinario.setEstado(true);

			veterinarioRepo.save(veterinario);
			System.out.println("✅ VETERINARIO GUARDADO EN BD: " + nombres + " " + apellidos);

			redirectAttributes.addFlashAttribute("mensaje", "Veterinario registrado correctamente");

		} catch (Exception e) {
			System.out.println("❌ ERROR AL REGISTRAR VETERINARIO EN BD: " + e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error al registrar veterinario: " + e.getMessage());
		}

		return "redirect:/admin#users";
	}

	// ✅ MÉTODO PARA REGISTRAR VETERINARIAS
	@PostMapping("/veterinarias/registrar")
	public String registrarVeterinaria(@RequestParam String nombre, @RequestParam String rut,
			@RequestParam String direccion, @RequestParam String telefono, @RequestParam String correo,
			@RequestParam String horario, @RequestParam String descripcion, @RequestParam String estado,
			RedirectAttributes redirectAttributes) {
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

			// ✅ Guardar en BD
			veterinariaRepo.save(veterinaria);

			System.out.println("✅ VETERINARIA GUARDADA EN BD: " + nombre);
			redirectAttributes.addFlashAttribute("mensaje", "Veterinaria registrada correctamente");

		} catch (Exception e) {
			System.out.println("❌ ERROR AL REGISTRAR VETERINARIA EN BD: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Error al registrar veterinaria: " + e.getMessage());
		}
		return "redirect:/admin#vets";
	}

	// ✅ MÉTODO PARA ACTUALIZAR PERFIL ADMIN
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
	public String subirImagen(@RequestParam("imagen") MultipartFile archivoImagen,
			RedirectAttributes redirectAttributes) {
		try {
			if (archivoImagen.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Por favor seleccione una imagen");
				return "redirect:/admin#profile";
			}

			// Validar tipo de archivo
			String contentType = archivoImagen.getContentType();
			if (contentType == null || !contentType.startsWith("image/")) {
				redirectAttributes.addFlashAttribute("error", "Por favor seleccione un archivo de imagen válido");
				return "redirect:/admin#profile";
			}

			// Convertir imagen a Base64
			byte[] bytesImagen = archivoImagen.getBytes();
			String imagenBase64 = Base64.getEncoder().encodeToString(bytesImagen);
			String imagenDataURL = "data:" + contentType + ";base64," + imagenBase64;

			// Actualizar el admin en BD
			Optional<PerfilAdmin> adminOptional = adminService.buscarPorId(1);

			if (adminOptional.isPresent()) {
				PerfilAdmin admin = adminOptional.get();
				admin.setImagen(imagenDataURL);
				adminService.guardar(admin);

				System.out.println("✅ IMAGEN DE PERFIL GUARDADA EN BD");
				redirectAttributes.addFlashAttribute("mensaje", "Imagen actualizada correctamente");
			} else {
				redirectAttributes.addFlashAttribute("error", "Administrador no encontrado");
			}

		} catch (IOException e) {
			System.out.println("❌ Error al procesar la imagen: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Error al procesar la imagen");
		} catch (Exception e) {
			System.out.println("❌ Error al subir imagen a BD: " + e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Error al subir la imagen");
		}

		return "redirect:/admin#profile";
	}

	// MÉTODOS AUXILIARES PARA CARGAR DATOS DESDE BD
	private void cargarUsuariosParaVista(Model model) {
		List<Usuario> usuarios = usuarioRepo.findAll();
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

	private PerfilAdmin obtenerAdminLogueado() {
		try {
			Optional<PerfilAdmin> adminOptional = adminService.buscarPorId(1);

			if (adminOptional.isPresent()) {
				return adminOptional.get();
			} else {
				// Crear admin por defecto si no existe
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
}