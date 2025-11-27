package com.clinicpet.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List; // *** AGREGADO: Para List<Mascota> en perfil ***
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.clinicpet.demo.dto.ProductoDTO;
import com.clinicpet.demo.dto.VeterinariaDTO;
import com.clinicpet.demo.model.Inventario;
import com.clinicpet.demo.model.Mascota;
import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.model.Veterinaria;
import com.clinicpet.demo.repository.IInventarioRepository;
import com.clinicpet.demo.repository.IVeterinariaRepository;
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

	@Autowired
	private IVeterinariaRepository veterinariaRepository;

	@Autowired
	private IInventarioRepository inventarioRepository;

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

					// ✅ REDIRIGIR SEGÚN ROL
					Integer rolId = usuario.getRol().getId();

					if (rolId == 3) { // Administrador
						return "redirect:/admin";
					} else if (rolId == 2) { // Veterinario
						return "redirect:/perfil-veterinario";
					} else { // Usuario normal (rol 1)
						return "redirect:/usuarios/inicio";
					}
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
			redirectAttributes.addFlashAttribute("mensaje", "¡Registro exitoso! Bienvenido a HelpYourPet");

			// *** CORRECCIÓN: Redirigir al INICIO (página principal) ***
			return "redirect:/"; // Redirige a la página de inicio

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

		// ✅ AGREGAR ESTA LÍNEA - Pasar el ID al modelo
		model.addAttribute("idUsuarioActual", usuarioLogueado.getId());

		model.addAttribute("mascota", new Mascota());

		// Carga lista de mascotas del usuario logueado
		List<Mascota> mascotas = mascotaService.buscarPorUsuario(usuarioLogueado.getId());
		model.addAttribute("mascotas", mascotas);
		model.addAttribute("tieneMascotas", !mascotas.isEmpty());

		// Para HTML (saludo, foto usuario, etc.)
		model.addAttribute("usuarioLogueado", usuarioLogueado);

		return "Usuario/perfilusuario";
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

		// ✅ Verificar que la mascota pertenece al usuario
		if (!mascota.getUsuario().getId().equals(usuarioLogueado.getId())) {
			LOGGER.warn("❌ Usuario {} intentó acceder a mascota {} que no le pertenece", usuarioLogueado.getId(), id);
			return ResponseEntity.status(403).build();
		}

		LOGGER.info("✅ Mascota encontrada: {} - Unidad Edad: {}", mascota.getNombre(), mascota.getUnidadEdad());
		return ResponseEntity.ok(mascota);
	}

	// Agregar mascota (corregido: agregado check null y logs debug básicos)
	@PostMapping("/perfilusuario/agregarMascota")
	public String agregarMascota(@ModelAttribute Mascota mascota, @RequestParam("idUsuario") Integer idUsuario,
			@RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
			RedirectAttributes redirectAttributes) {

		try {
			if (idUsuario == null) {
				redirectAttributes.addFlashAttribute("error", "ID de usuario inválido");
				return "redirect:/usuarios/perfilusuario";
			}

			Optional<Usuario> optionalUsuario = usuarioService.buscarUsuarioPorId(idUsuario);
			if (optionalUsuario.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
				return "redirect:/usuarios/perfilusuario";
			}

			Usuario usuarioExistente = optionalUsuario.get();
			mascota.setUsuario(usuarioExistente);

			if (fotoFile != null && !fotoFile.isEmpty()) {
				String uploadDir = System.getProperty("user.dir") + "/uploads/";
				String fileName = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename();

				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				Path filePath = uploadPath.resolve(fileName);
				Files.copy(fotoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				mascota.setFoto("/uploads/" + fileName);
			} else {
				mascota.setFoto("/uploads/default_pet.png"); // Asegúrate que exista esta imagen por defecto
			}

			mascotaService.guardarMascota(mascota);
			redirectAttributes.addFlashAttribute("success",
					"Mascota '" + mascota.getNombre() + "' agregada correctamente");

		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", "Error al subir la foto: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al agregar mascota: " + e.getMessage());
			e.printStackTrace();
		}

		return "redirect:/usuarios/perfilusuario";
	}

	@PostMapping("/perfilusuario/actualizarMascota")
	public String actualizarMascota(@ModelAttribute Mascota mascota,
			@RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
			RedirectAttributes redirectAttributes, HttpSession session) {

		try {
			if (mascota.getId() == null) {
				redirectAttributes.addFlashAttribute("error", "ID de mascota requerido para actualización.");
				return "redirect:/usuarios/perfilusuario";
			}

			Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
			if (usuarioLogueado == null) {
				return "redirect:/usuarios/login";
			}

			Optional<Mascota> mascotaExistenteOpt = mascotaService.buscarMascotaPorId(mascota.getId());
			if (mascotaExistenteOpt.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Mascota no encontrada.");
				return "redirect:/usuarios/perfilusuario";
			}

			Mascota mascotaExistente = mascotaExistenteOpt.get();

			if (!mascotaExistente.getUsuario().getId().equals(usuarioLogueado.getId())) {
				redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar esta mascota.");
				return "redirect:/usuarios/perfilusuario";
			}

			if (fotoFile != null && !fotoFile.isEmpty()) {
				String uploadDir = System.getProperty("user.dir") + "/uploads/";
				String fileName = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename();

				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				Path filePath = uploadPath.resolve(fileName);
				Files.copy(fotoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				mascota.setFoto("/uploads/" + fileName);
			} else {
				// Mantener la foto actual si no se sube una nueva
				mascota.setFoto(mascotaExistente.getFoto());
			}

			mascota.setUsuario(usuarioLogueado); // Asegura que la mascota mantenga el usuario
			mascotaService.actualizarMascota(mascota);

			redirectAttributes.addFlashAttribute("success",
					"Mascota '" + mascota.getNombre() + "' actualizada correctamente");

		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", "Error al subir la foto: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al actualizar mascota: " + e.getMessage());
			e.printStackTrace();
		}

		return "redirect:/usuarios/perfilusuario";
	}

	@DeleteMapping("/perfilusuario/eliminarmascota/{id}")
	public ResponseEntity<String> eliminarMascota(@PathVariable Integer id, HttpSession session) {
		try {
			Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
			if (usuarioLogueado == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
			}
			mascotaService.eliminarMascota(id);
			return ResponseEntity.ok("Mascota eliminada exitosamente");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
		}
	}

	// Endpoint para actualizar foto de perfil
	@PostMapping("/perfilusuario/actualizarFotoPerfil")
	@ResponseBody
	public ResponseEntity<?> actualizarFotoPerfil(@RequestBody Map<String, String> request) {
		try {
			String fotoBase64 = request.get("fotoPerfil");
			Long usuarioId = Long.parseLong(request.get("usuarioId"));

			// Validar y procesar la imagen
			if (fotoBase64 != null && !fotoBase64.isEmpty()) {
				// Decodificar Base64 y guardar la imagen
				String rutaFoto = guardarImagenDesdeBase64(fotoBase64, usuarioId);

				// Actualizar en la base de datos
				usuarioService.actualizarFotoPerfil(usuarioId, rutaFoto);

				return ResponseEntity.ok().body(Map.of("mensaje", "Foto actualizada correctamente"));
			}

			return ResponseEntity.badRequest().body(Map.of("error", "Datos de imagen inválidos"));

		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("error", "Error al actualizar la foto: " + e.getMessage()));
		}
	}

	// Endpoint para eliminar foto de perfil
	@PostMapping("/perfilusuario/eliminarFotoPerfil")
	@ResponseBody
	public ResponseEntity<?> eliminarFotoPerfil(@RequestBody Map<String, String> request) {
		try {
			Long usuarioId = Long.parseLong(request.get("usuarioId"));

			// Eliminar foto y establecer por defecto
			usuarioService.eliminarFotoPerfil(usuarioId);

			return ResponseEntity.ok().body(Map.of("mensaje", "Foto eliminada correctamente"));

		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(Map.of("error", "Error al eliminar la foto: " + e.getMessage()));
		}
	}

	// Método auxiliar para guardar imágenes
	private String guardarImagenDesdeBase64(String base64Image, Long usuarioId) {
		try {
			// Eliminar el prefijo data:image/...;base64,
			String[] parts = base64Image.split(",");
			String imageString = parts[1];

			// Decodificar Base64
			byte[] imageBytes = Base64.getDecoder().decode(imageString);

			// Sin restricción de tamaño - permitir cualquier peso de imagen
			System.out.println(" Procesando imagen de " + (imageBytes.length / (1024 * 1024)) + "MB");

			// Crear directorio si no existe
			String uploadDir = "uploads/profiles/";
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// Generar nombre único
			String fileName = "profile_" + usuarioId + "_" + System.currentTimeMillis() + ".webp";
			Path filePath = uploadPath.resolve(fileName);

			// Guardar archivo
			Files.write(filePath, imageBytes);

			return "/" + uploadDir + fileName;

		} catch (Exception e) {
			throw new RuntimeException("Error al guardar la imagen: " + e.getMessage(), e);
		}
	}

	@PostMapping("/usuario/{id}/foto")
	public ResponseEntity<?> actualizarFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

		try {
			String ruta = usuarioService.guardarFoto(id, file);
			return ResponseEntity.ok(ruta);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al subir la imagen");
		}
	}

	@DeleteMapping("/usuario/{id}/foto")
	public ResponseEntity<?> eliminarFoto(@PathVariable Long id) {
		usuarioService.eliminarFotoPerfil(id);
		return ResponseEntity.ok("Foto eliminada");
	}

	@PostMapping("/perfilusuario/actualizarInfoPersonal")
	public String actualizarInfoPersonal(@ModelAttribute("usuarioLogueado") Usuario usuarioForm, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Usuario usuarioSession = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuarioSession == null) {
			return "redirect:/usuarios/iniciarsesion";
		}

		try {
			Usuario actualizado = usuarioService.actualizarUsuario(usuarioForm.getId(), usuarioForm);

			if (actualizado != null) {
				session.setAttribute("usuarioLogueado", actualizado);
				redirectAttributes.addFlashAttribute("success", "Información de perfil actualizada correctamente");
			} else {
				redirectAttributes.addFlashAttribute("error", "No se encontró el usuario para actualizar");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al actualizar la información: " + e.getMessage());
		}

		return "redirect:/usuarios/perfilusuario";
	}

	@PutMapping("/usuario/actualizar/{id}")
	public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id,
			@RequestBody Usuario usuarioActualizado) {

		Usuario u = usuarioService.actualizarUsuario(id, usuarioActualizado);
		return ResponseEntity.ok(u);
	}

	@PutMapping("/usuario/{id}/password")
	public ResponseEntity<?> actualizarPassword(@PathVariable Integer id, @RequestBody String nuevaPassword) {

		usuarioService.actualizarPassword(id, nuevaPassword);
		return ResponseEntity.ok("Contraseña actualizada");
	}

	// redireccion cambiar contraseña
	@GetMapping("/recovery")
	public String recovery() {
		return "RecuperarContrasena/recovery";
	}

	// Endpoint para eliminar cuenta de usuario
	@DeleteMapping("/perfilusuario/eliminarCuenta")
	@ResponseBody
	public ResponseEntity<?> eliminarCuenta(HttpSession session) {
		try {
			Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
			if (usuarioLogueado == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No hay usuario logueado"));
			}

			// Eliminar el usuario de la base de datos
			usuarioService.eliminarUsuario(usuarioLogueado.getId());

			// Invalidar la sesión
			session.invalidate();

			return ResponseEntity.ok().body(Map.of("mensaje", "Cuenta eliminada exitosamente"));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Error al eliminar la cuenta: " + e.getMessage()));
		}
	}

	// REDIRECCION PARA LA VISTA ADOPCIONES
	@GetMapping("/adopcion")
	public String mostrarAdopciones(HttpSession session, Model model) {

		Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuarioLogueado == null) {
			return "redirect:/usuarios/iniciarsesion";
		}
		model.addAttribute("usuarioLogueado", usuarioLogueado);

		return "Adopciones/adopcion";
	}

	// redireccion al cerrar sesion
	@GetMapping("/index")
	public String index() {
		return "/index";
	}

	//redireccion a tienda
	@GetMapping("/tienda")
	public String tienda() {
		return "Tienda/tienda";
	}
	
	//redireccion a pasarela de pagos
	@GetMapping("/pasarela-pagos")
	public String pasarelaPagos() {
		return "Tienda/pasarela-pagos";
	}

	// API para obtener todas las veterinarias/tiendas
	@GetMapping("/api/veterinarias")
	@ResponseBody
	public ResponseEntity<List<VeterinariaDTO>> obtenerVeterinarias() {
		try {
			List<Veterinaria> veterinarias = veterinariaRepository.findAll();
			
			// Convertir entidades a DTOs para evitar problemas de lazy loading
			List<VeterinariaDTO> veterinariasDTO = veterinarias.stream()
				.map(v -> new VeterinariaDTO(
					v.getId(),
					v.getNombre(),
					v.getDireccion(),
					v.getTelefono(),
					v.getCorreo(),
					v.getHorario(),
					v.getDescripcion(),
					v.getEstado()
				))
				.collect(Collectors.toList());
			
			LOGGER.info("✅ Se encontraron {} veterinarias", veterinariasDTO.size());
			return ResponseEntity.ok(veterinariasDTO);
		} catch (Exception e) {
			LOGGER.error("❌ Error al obtener veterinarias: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// API para obtener productos de una veterinaria específica
	@GetMapping("/api/veterinarias/{veterinariaId}/productos")
	@ResponseBody
	public ResponseEntity<List<ProductoDTO>> obtenerProductosPorVeterinaria(@PathVariable Integer veterinariaId) {
		try {
			LOGGER.info("🔍 Buscando productos para veterinaria ID: {}", veterinariaId);
			
			// Verificar que la veterinaria existe
			Optional<Veterinaria> veterinariaOpt = veterinariaRepository.findById(veterinariaId);
			if (veterinariaOpt.isEmpty()) {
				LOGGER.warn("❌ Veterinaria no encontrada con ID: {}", veterinariaId);
				return ResponseEntity.notFound().build();
			}
			
			Veterinaria veterinaria = veterinariaOpt.get();
			
			// Obtener inventario de la veterinaria
			List<Inventario> inventarios = inventarioRepository.findByVeterinaria(veterinaria);
			
			// Convertir a DTOs con información del producto y stock
			List<ProductoDTO> productosDTO = inventarios.stream()
				.filter(inv -> inv.getCantidadDisponible() != null && inv.getCantidadDisponible() > 0) // Solo productos disponibles
				.map(inv -> new ProductoDTO(
					inv.getProducto().getId(),
					inv.getProducto().getNombre(),
					inv.getProducto().getDescripcion(),
					inv.getProducto().getPrecio(),
					inv.getProducto().getImagen(),
					inv.getProducto().getCategoria(),
					inv.getCantidadDisponible(),
					inv.getEstado()
				))
				.collect(Collectors.toList());
			
			LOGGER.info("✅ Se encontraron {} productos disponibles para veterinaria '{}'", 
				productosDTO.size(), veterinaria.getNombre());
			
			return ResponseEntity.ok(productosDTO);
			
		} catch (Exception e) {
			LOGGER.error("❌ Error al obtener productos de veterinaria {}: {}", veterinariaId, e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}