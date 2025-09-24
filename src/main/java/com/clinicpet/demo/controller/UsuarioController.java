package com.clinicpet.demo.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
			return "iniciarsesion"; // Corregido: unificado a "iniciarsesion"
		} catch (Exception e) {
			model.addAttribute("error", "Error al iniciar sesión: " + e.getMessage());
			model.addAttribute("usuarioLogin", usuarioLogin != null ? usuarioLogin : new Usuario()); // Consistente:
																										// prefiere
																										// datos
																										// ingresados
			return "iniciarsesion"; // Corregido: unificado a "iniciarsesion"
		}
	}

	// Mostrar inicio (dashboard)
	@GetMapping("/inicio")
	public String mostrarInicio(Model model, HttpSession session) {
		Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuarioLogueado == null) {
			return "redirect:/usuarios/iniciarsesion";
		}
		model.addAttribute("usuario", usuarioLogueado);
		model.addAttribute("mensaje", "Bienvenido al Dashboard ClinicPet");
		return "Inicio/inicio";
	}

	// Mostrar formulario de registro
	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "Registro/registro"; // Simplificado: templates/registro.html (sin subcarpeta)
	}

	// Procesar formulario de registro
	@PostMapping("/registro")
	public String procesarRegistro(@ModelAttribute Usuario usuario, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
			redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado con éxito");
			return "redirect:/usuarios/iniciarsesion";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("usuario", usuario);
			return "Registro/registro";
		}
	}

	// Mostrar perfil de usuario
	@GetMapping("/perfilusuario")
	public String perfilusuario(Model model, HttpSession session) {
		Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuarioLogueado == null) {
			return "redirect:/usuarios/iniciarsesion";
		}
		Integer idUsuarioActual = usuarioLogueado.getId(); // De sesión (mejor que hardcode)
		model.addAttribute("idUsuarioActual", idUsuarioActual);
		model.addAttribute("mascota", new Mascota());
		return "Usuario/perfilusuario"; // templates/perfilusuario.html
	}

	// Index simple
	@GetMapping("/index")
	public String index() {
		return "index";
	}

	// Agregar mascota (simplificado: removidos comentarios redundantes)
	@PostMapping("/perfilusuario/agregarMascota")
	public String agregarMascota(@ModelAttribute Mascota mascota, @RequestParam("idUsuario") Integer idUsuario,
			@RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Intentando agregar mascota para usuario ID: {}", idUsuario);
		Optional<Usuario> optionalUsuario = usuarioService.buscarUsuarioPorId(idUsuario);
		if (optionalUsuario.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
			return "redirect:/usuarios/perfilusuario";
		}
		Usuario usuarioExistente = optionalUsuario.get();
		mascota.setUsuario(usuarioExistente);
		if (mascota.getEstado() == null || mascota.getEstado().isEmpty()) {
			mascota.setEstado("disponible");
		}
		try {
			// Manejo de foto
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
		} catch (Exception e) {
			LOGGER.error("Error al guardar mascota: {}", e.getMessage(), e);
			redirectAttributes.addFlashAttribute("error", "Error al agregar mascota: " + e.getMessage());
		}
		return "redirect:/usuarios/perfilusuario";
	}
}