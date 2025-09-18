package com.clinicpet.demo.controller;

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
	public String procesarLogin(@RequestParam String username, @RequestParam String contraseña, Model model) {
		LOGGER.info("Intentando iniciar sesión con usuario: {}", username);

		if (usuarioService.validarCredenciales(username, contraseña)) {
			// ✅ Login exitoso
			return "redirect:/usuarios/inicio";
		} else {
			// ❌ Credenciales incorrectas
			model.addAttribute("error", "Usuario o contraseña incorrectos");
			return "IniciarSesion/iniciarsesion";
		}
	}

	@GetMapping("/inicio")
	public String inicio() {
		return "Inicio/inicio";
	}

	@GetMapping("/registro")
	public String registro() {
		return "Registro/registro";
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
