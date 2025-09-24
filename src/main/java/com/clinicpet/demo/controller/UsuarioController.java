package com.clinicpet.demo.controller;

import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.service.IUsuarioService;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

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
	public String perfilusuario() {
		return "Usuario/perfilusuario";
	}

	@GetMapping("/index")
	public String index() {
		return "index";
	}
}
