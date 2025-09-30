package com.clinicpet.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List; // *** AGREGADO: Para List<Mascota> en perfil ***
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.clinicpet.demo.model.Mascota;
import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.service.IMascotaService;
import com.clinicpet.demo.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IMascotaService mascotaService;

	// Mostrar formulario de login (unificado a vista "iniciarsesion")
	@GetMapping("/iniciarsesion")
	public String mostrarLogin(Model model) {
		model.addAttribute("usuarioLogin", new Usuario()); // Para binding en form
		return "IniciarSesion/iniciarsesion"; // Unificado: templates/iniciarsesion.html (ajusta si usas subcarpeta)
	}

	// Procesar login
	@PostMapping("/iniciarsesion")
	public String procesarLogin(@ModelAttribute Usuario usuarioLogin, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorCorreo(usuarioLogin.getCorreo());
			if (usuarioOpt.isPresent()) {
				Usuario usuario = usuarioOpt.get();
				if (usuario.getPassword().equals(usuarioLogin.getPassword()) && usuario.isActivo()) {
					session.setAttribute("usuarioLogueado", usuario);
					redirectAttributes.addFlashAttribute("mensaje", "Bienvenido, " + usuario.getNombres());
					return "redirect:/usuarios/inicio"; // Corregido: coincide con mapeo /usuarios/inicio
				}
			}
			// Bloque unificado para errores (evita duplicación)
			model.addAttribute("error", "Correo o contraseña incorrectos, o usuario inactivo");
			model.addAttribute("usuarioLogin", usuarioLogin); // Rellena campos en error
			return "IniciarSesion/iniciarsesion"; // Corregido: unificado a "iniciarsesion"
		} catch (Exception e) {
			model.addAttribute("error", "Error al iniciar sesión: " + e.getMessage());
			model.addAttribute("usuarioLogin", usuarioLogin != null ? usuarioLogin : new Usuario()); // Consistente:
																										// prefiere
																										// datos
																										// ingresados
			return "IniciarSesion/iniciarsesion"; // Corregido: unificado a "iniciarsesion"
		}
	}

	// Mostrar inicio (dashboard)
	@GetMapping("/inicio")
	public String mostrarInicio(Model model, HttpSession session) {
		Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuarioLogueado == null) {
			return "usuarios/iniciarsesion";
		}
		model.addAttribute("usuario", usuarioLogueado);
		model.addAttribute("mensaje", "Bienvenido al Dashboard ClinicPet");
		return "Inicio/inicio";
	}

	// Mostrar formulario de registro
	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model model) {
		model.addAttribute("usuario", new Usuario());
		// *** CORRECCIÓN BÁSICA: Usar subcarpeta para coincidir con tu estructura ***
		return "Registro/registro"; // Busca templates/Registro/registro.html
	}

	// Procesar formulario de registro
	@PostMapping("/registro")
	public String procesarRegistro(@ModelAttribute Usuario usuario, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			// *** CORRECCIÓN BÁSICA: Llamar al service corregido (maneja rol y
			// validaciones) ***
			Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
			redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado con éxito");

			// *** CORRECCIÓN BÁSICA: Redirigir al INICIO (página principal) ***
			return "IniciarSesion/iniciarsesion"; // Cambia "/" por tu ruta de inicio si es diferente (ej.
													// "redirect:/home")

		} catch (RuntimeException e) {
			// *** CORRECCIÓN BÁSICA: Usar la misma ruta con subcarpeta para consistencia
			// ***
			model.addAttribute("error", e.getMessage());
			model.addAttribute("usuario", usuario); // Preserva los datos del form
			return "Registro/registro"; // Coincide con el GET: templates/Registro/registro.html
		}
	}

	// Mostrar perfil de usuario (corregido: agregado log debug y usuarioLogueado a
	// model)
	@GetMapping("/perfilusuario")
	public String perfilusuario(Model model, HttpSession session) {
		Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuarioLogueado == null) {
			return "redirect:/usuarios/iniciarsesion";
		}
		Integer idUsuarioActual = usuarioLogueado.getId(); // De sesión (mejor que hardcode)
		model.addAttribute("idUsuarioActual", idUsuarioActual);
		model.addAttribute("mascota", new Mascota());

		// *** AGREGADO BÁSICO: Carga lista de mascotas del usuario logueado ***
		List<Mascota> mascotas = mascotaService.buscarPorUsuario(idUsuarioActual);
		model.addAttribute("mascotas", mascotas); // Para mostrar en HTML (ej. tabla con th:each)
		model.addAttribute("tieneMascotas", !mascotas.isEmpty()); // Booleano simple para UI (opcional)

		// *** AGREGADO: Log debug básico (confirma carga)
		System.out.println("DEBUG CONTROLLER: Cargando perfil para usuario ID=" + idUsuarioActual
				+ ", Mascotas encontradas: " + mascotas.size());

		// *** AGREGADO: Para HTML (saludo, foto usuario, etc.)
		model.addAttribute("usuarioLogueado", usuarioLogueado);

		return "Usuario/perfilusuario"; // templates/perfilusuario.html
	}

	@GetMapping("/perfilusuario/mascota/{id}")
	@ResponseBody
	public ResponseEntity<Mascota> obtenerMascotaPorId(@PathVariable Integer id, HttpSession session) {
		LOGGER.info("🔍 Buscando mascota con ID: {}", id);

		Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuarioLogueado == null) {
			LOGGER.warn("❌ Usuario no logueado");
			return ResponseEntity.status(401).build();
		}

		Optional<Mascota> mascotaOpt = mascotaService.buscarMascotaPorId(id);
		if (mascotaOpt.isEmpty()) {
			LOGGER.warn("❌ Mascota no encontrada con ID: {}", id);
			return ResponseEntity.notFound().build();
		}

		Mascota mascota = mascotaOpt.get();

		if (!mascota.getUsuario().getId().equals(usuarioLogueado.getId())) {
			LOGGER.warn("❌ Usuario {} intentó acceder a mascota {} que no le pertenece", usuarioLogueado.getId(), id);
			return ResponseEntity.status(403).build();
		}

		LOGGER.info("✅ Mascota encontrada: {}", mascota.getNombre());
		return ResponseEntity.ok(mascota);
	}

	// Agregar mascota (corregido: agregado check null y logs debug básicos)
	@PostMapping("/perfilusuario/agregarMascota")
	public String agregarMascota(@ModelAttribute Mascota mascota, @RequestParam("idUsuario") Integer idUsuario,
			@RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
			RedirectAttributes redirectAttributes) {

		LOGGER.info("Intentando agregar mascota para usuario ID: {}", idUsuario);

		// *** AGREGADO: Check básico null para idUsuario (evita Optional.empty)
		if (idUsuario == null) {
			LOGGER.error("ID Usuario null en request");
			redirectAttributes.addFlashAttribute("error", "ID de usuario inválido");
			return "redirect:/usuarios/perfilusuario";
		}

		// *** AGREGADO: Log debug para confirmar ID válido
		System.out.println("DEBUG CONTROLLER: Agregando mascota para usuario ID=" + idUsuario);

		Optional<Usuario> optionalUsuario = usuarioService.buscarUsuarioPorId(idUsuario);
		if (optionalUsuario.isEmpty()) {
			LOGGER.error("Usuario no encontrado para ID: {}", idUsuario);
			redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
			return "redirect:/usuarios/perfilusuario";
		}
		Usuario usuarioExistente = optionalUsuario.get();
		mascota.setUsuario(usuarioExistente);

		// *** AGREGADO: Log debug antes de save (confirma FK seteado)
		System.out.println("DEBUG CONTROLLER: Seteando usuario ID=" + usuarioExistente.getId() + " para mascota '"
				+ mascota.getNombre() + "'");

		try {
			// Manejo de foto (sin cambios)
			if (fotoFile != null && !fotoFile.isEmpty()) {
				String uploadDir = "src/main/resources/static/images/mascotas/";
				String fileName = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename();
				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(fotoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				mascota.setFoto("/images/mascotas/" + fileName);
			} else {
				mascota.setFoto("/images/mascotas/default_pet.png");
			}
			mascotaService.guardarMascota(mascota);
			redirectAttributes.addFlashAttribute("success", "Mascota '" + mascota.getNombre() + "' agregada!");

			LOGGER.info("Mascota '{}' agregada correctamente para usuario ID: {}", mascota.getNombre(), idUsuario);

		} catch (Exception e) {
			LOGGER.error("Error al guardar mascota: {}", e.getMessage(), e);
			redirectAttributes.addFlashAttribute("error", "Error al agregar mascota: " + e.getMessage());
		}
		return "redirect:/usuarios/perfilusuario";
	}

	// Corrección completa: Eliminar mascota (integra verificación con buscarPorId)
	@PostMapping("/perfilusuario/eliminarMascota")
	@ResponseBody
	public ResponseEntity<?> eliminarMascota(@RequestParam Integer id) { // Cambiado a Integer
		try {
			// Corrección de línea 1: Usar nombre correcto y verificar existencia primero
			Optional<Mascota> mascotaOpcional = mascotaService.buscarMascotaPorId(id);
			if (mascotaOpcional.isEmpty()) {
				return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Mascota no encontrada"));
			}

			mascotaService.eliminarMascota(id); // Nombre correcto: eliminarMascota(Integer id)
			return ResponseEntity.ok(Map.of("success", true, "message", "Mascota eliminada correctamente"));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(Map.of("success", false, "message", e.getMessage()));
		}
	}

	@PostMapping("/perfilusuario/actualizarMascota")
	public String actualizarMascota(@ModelAttribute Mascota mascota,
			@RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
			@RequestParam(value = "fotoActual", required = false) String fotoActual,
			RedirectAttributes redirectAttributes, HttpSession session) {

		LOGGER.info("=== INICIO ACTUALIZACIÓN MASCOTA ===");
		LOGGER.info("ID mascota recibida: {}", mascota.getId());
		LOGGER.info("Nombre recibido: {}", mascota.getNombre());

		try {
			// ✅ 1. Validar ID
			if (mascota.getId() == null) {
				redirectAttributes.addFlashAttribute("error", "ID de mascota requerido para actualización.");
				return "redirect:/usuarios/perfilusuario";
			}

			// ✅ 2. Validar usuario logueado
			Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
			if (usuarioLogueado == null) {
				LOGGER.warn("Usuario no logueado intentando actualizar mascota");
				return "redirect:/usuarios/login";
			}

			// ✅ 3. Verificar que la mascota existe y pertenece al usuario
			Optional<Mascota> mascotaExistenteOpt = mascotaService.buscarMascotaPorId(mascota.getId());
			if (mascotaExistenteOpt.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Mascota no encontrada.");
				return "redirect:/usuarios/perfilusuario";
			}

			Mascota mascotaExistente = mascotaExistenteOpt.get();

			// ✅ 4. Verificar permisos
			if (!mascotaExistente.getUsuario().getId().equals(usuarioLogueado.getId())) {
				LOGGER.warn("Usuario {} intentó editar mascota {} que no le pertenece", usuarioLogueado.getId(),
						mascota.getId());
				redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar esta mascota.");
				return "redirect:/usuarios/perfilusuario";
			}

			// ✅ 5. Manejo de la foto
			if (fotoFile != null && !fotoFile.isEmpty()) {
				LOGGER.info("Subiendo nueva foto para mascota ID: {}", mascota.getId());

				String fileName = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename();
				Path uploadPath = Paths.get("src/main/resources/static/images/mascotas");

				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				Files.copy(fotoFile.getInputStream(), uploadPath.resolve(fileName),
						StandardCopyOption.REPLACE_EXISTING);

				mascota.setFoto("/images/mascotas/" + fileName);
				LOGGER.info("Foto subida exitosamente: {}", fileName);
			} else {
				// ✅ NO establecer foto aquí, el Service la mantendrá
				LOGGER.info("No se subió nueva foto, se mantendrá la actual");
				mascota.setFoto(null); // El service ignorará este null y mantendrá la foto actual
			}

			// ✅ 6. Actualizar mascota (el Service maneja el usuario y la foto si es null)
			Mascota mascotaActualizada = mascotaService.actualizarMascota(mascota);

			redirectAttributes.addFlashAttribute("success",
					"¡Mascota '" + mascotaActualizada.getNombre() + "' actualizada correctamente!");
			LOGGER.info("=== MASCOTA ACTUALIZADA EXITOSAMENTE: ID={} ===", mascotaActualizada.getId());

		} catch (IllegalArgumentException e) {
			LOGGER.error("Error de validación: {}", e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error al subir foto: {}", e.getMessage(), e);
			redirectAttributes.addFlashAttribute("error", "Error al subir la foto. Intenta de nuevo.");
		} catch (Exception e) {
			LOGGER.error("Error inesperado al actualizar mascota ID {}: {}", mascota.getId(), e.getMessage(), e);
			redirectAttributes.addFlashAttribute("error", "Error al actualizar mascota. Intenta de nuevo.");
		}

		return "redirect:/usuarios/perfilusuario";
	}

//redireccion al cerrar sesion
	@GetMapping("/index")
	public String index() {
		return "/index";
	}
}
