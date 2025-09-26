package com.clinicpet.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/veterinario")
public class VeterinarioController {
	@GetMapping("/dashboard")
	public String dashboard() {
		return "Veterinario/dashboard"; // Vista HTML para veterinario
	}

}
