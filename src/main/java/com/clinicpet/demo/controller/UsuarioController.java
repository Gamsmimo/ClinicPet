package com.clinicpet.demo.controller;

import org.springframework.web.multipart.MultipartFile; // Importar esta clase

import com.clinicpet.demo.model.Mascota; // Importar la clase Mascota
import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.service.IMascotaService; // Importar el servicio de Mascota
import com.clinicpet.demo.service.IUsuarioService;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Para mensajes flash

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IMascotaService mascotaService; // ¡Inyectar el servicio de Mascota!

	// Mostrar formulario de login
	@GetMapping("/iniciarsesion")
	public String iniciarsesion() {
		return "IniciarSesion/iniciarsesion";
	}

	// Procesar login
	@PostMapping("/iniciarsesion")
	public String procesarLogin(@RequestParam String correo, @RequestParam String password, Model model) {
		LOGGER.info("Intentando iniciar sesión con correo: {}", correo);

		if (usuarioService.validarCredencialesPorCorreo(correo, password)) {
			// Obtener el usuario para verificar su rol
			Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorCorreo(correo);
			if (usuarioOpt.isPresent()) {
				Usuario usuario = usuarioOpt.get();

				// Verificar que el rol no sea null
				if (usuario.getRol() == null) {
					model.addAttribute("error", "Usuario sin rol asignado");
					return "IniciarSesion/iniciarsesion";
				}

				Integer rolId = usuario.getRol().getId();

				// Redirigir según el rol (ajusta los endpoints según tus vistas)
				switch (rolId) {
				case 1: // Usuario normal (ID 1 en tabla rol)
					return "redirect:/usuarios/perfilusuario";
				case 2: // Veterinario (ID 2)
					return "redirect:/veterinario/dashboard";
				case 3: // Administrador (ID 3)
					return "redirect:/admin/panel";
				default:
					model.addAttribute("error", "Rol no válido");
					return "IniciarSesion/iniciarsesion";
				}
			} else {
				model.addAttribute("error", "Usuario no encontrado");
				return "IniciarSesion/iniciarsesion";
			}
		}

		// Credenciales incorrectas
		model.addAttribute("error", "Correo o contraseña incorrectos");
		return "IniciarSesion/iniciarsesion";
	}

	@GetMapping("/inicio")
	public String inicio() {
		return "Inicio/inicio";
	}

	// Mostrar formulario de registro
	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "Registro/registro"; // nombre de la vista HTML
	}

	// Procesar formulario de registro
	@PostMapping("/registro")
	public String procesarRegistro(@ModelAttribute Usuario usuario, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			// NO hagas: usuario.setRol(null); o asignaciones manuales aquí
			// El servicio asignará rol por defecto
			Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
			redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado con éxito");
			return "redirect:/usuarios/iniciarsesion";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage()); // Muestra error, e.g., "Rol por defecto no encontrado"
			model.addAttribute("usuario", usuario); // Rellena formulario
			return "Registro/registro"; // Vuelve al form
		}
	}

	@GetMapping("/perfilusuario")
	public String perfilusuario(Model model) {
		// **CRÍTICO:** Obtener el ID del usuario logueado.
		// Por ahora, para pruebas, puedes usar un ID fijo.
		// En una app real, esto vendría de la sesión, Spring Security, etc.
		Integer idUsuarioActual = 1; // <-- ¡CAMBIA ESTO POR EL ID REAL DEL USUARIO LOGUEADO!
		model.addAttribute("idUsuarioActual", idUsuarioActual);
		model.addAttribute("mascota", new Mascota()); // Objeto vacío para el formulario de agregar mascota
		return "Usuario/perfilusuario";
	}

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@PostMapping("/perfilusuario/agregarMascota")
	public String agregarMascota(@ModelAttribute Mascota mascota, @RequestParam("idUsuario") Integer idUsuario,
			@RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile, // <-- Nuevo parámetro para el
																						// archivo
			RedirectAttributes redirectAttributes, Model model) {
		LOGGER.info("Intentando agregar mascota para el usuario con ID: {}", idUsuario);
		LOGGER.info("Datos de la mascota recibidos: {}", mascota);
		Optional<Usuario> optionalUsuario = usuarioService.buscarUsuarioPorId(idUsuario);
		if (optionalUsuario.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "No se pudo encontrar el usuario para registrar la mascota.");
			return "redirect:/usuarios/perfilusuario";
		}
		Usuario usuarioExistente = optionalUsuario.get();
		mascota.setUsuario(usuarioExistente);
		if (mascota.getEstado() == null || mascota.getEstado().isEmpty()) {
			mascota.setEstado("disponible");
		}
		// Puedes añadir más lógica para tamaño, descripción si es necesario
		try {
			// --- Lógica para guardar el archivo ---
			if (fotoFile != null && !fotoFile.isEmpty()) {
				// Define la ruta donde guardarás las imágenes.
				// ¡IMPORTANTE! Esta ruta debe ser accesible por tu aplicación y persistente.
				// Para desarrollo, puedes usar una carpeta dentro del proyecto o una ruta
				// absoluta.
				// Para producción, considera un servicio de almacenamiento de objetos (S3,
				// Google Cloud Storage)
				// o una carpeta fuera del despliegue de la aplicación.
				String uploadDir = "src/main/resources/static/images/mascotas/"; // Ejemplo: dentro de static
				String fileName = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename(); // Nombre único
				Path uploadPath = Paths.get(uploadDir);
				// Crear el directorio si no existe
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				// Guardar el archivo en el sistema de archivos
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(fotoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				// Guardar la URL relativa de la imagen en el objeto Mascota
				// Esta URL será la que uses en el frontend para mostrar la imagen
				mascota.setFoto("/images/mascotas/" + fileName); // <-- Asigna la URL relativa
			} else {
				// Si no se sube ninguna foto, puedes asignar una imagen por defecto
				mascota.setFoto("/images/mascotas/default_pet.png"); // Asegúrate de tener una imagen por defecto
			}
			// --- Fin de la lógica para guardar el archivo ---
			mascotaService.guardarMascota(mascota);
			redirectAttributes.addFlashAttribute("success",
					"Mascota '" + mascota.getNombre() + "' agregada con éxito!");
		} catch (Exception e) {
			LOGGER.error("Error al guardar la mascota o la foto: {}", e.getMessage(), e); // Añadir 'e' para el stack
																							// trace
			redirectAttributes.addFlashAttribute("error", "Hubo un error al agregar la mascota: " + e.getMessage());
		}
		return "redirect:/usuarios/perfilusuario";
	}
}