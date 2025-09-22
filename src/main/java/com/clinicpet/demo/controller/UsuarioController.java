package com.clinicpet.demo.controller;

import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.service.IUsuarioService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
			// Login exitoso
			return "redirect:/usuarios/inicio";
		} else {
			// Credenciales incorrectas
			model.addAttribute("error", "Correo o contraseña incorrectos");
			return "IniciarSesion/iniciarsesion";
		}
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
	public String procesarRegistro(@ModelAttribute Usuario usuario,
			@RequestParam("confirmPassword") String confirmPassword, Model model) {
		System.out.println("Usuario recibido: " + usuario);

		if (usuario.getPassword() == null || !usuario.getPassword().equals(confirmPassword)) {
			model.addAttribute("error", "Las contraseñas no coinciden");
			return "Registro/registro";
		}

		usuarioService.save(usuario);
		System.out.println("Usuario guardado");

		return "redirect:/usuarios/iniciarsesion";
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
