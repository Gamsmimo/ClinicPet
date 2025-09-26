package com.clinicpet.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@GetMapping("/panel")
	public String panel() {
		return "Admin/panel"; // Vista HTML para administrador
	}
}
