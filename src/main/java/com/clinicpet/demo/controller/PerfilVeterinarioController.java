package com.clinicpet.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.clinicpet.demo.model.Cita;
import com.clinicpet.demo.model.Emergencia;
import com.clinicpet.demo.model.Inventario;
import com.clinicpet.demo.model.Mascota;
import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.model.Producto;
import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.model.Veterinaria;
import com.clinicpet.demo.service.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/perfil-veterinario")
public class PerfilVeterinarioController {

	@Autowired
	private IPerfilVeterinarioService perfilVeterinarioService;

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IMascotaService mascotaService;

	@Autowired
	private ICitaService citaService;

	@Autowired
	private HttpSession session;

	@Autowired
	private IInventarioService inventarioService;

	@Autowired
	private IVeterinariaService veterinariaService;

	@Autowired
	private IEmergenciaService emergenciaService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	
	

	// ==================== VISTA PRINCIPAL ====================
	@GetMapping
	public String mostrarPerfilVeterinario(Principal principal, Model model) {
	    System.out.println("🔍 Accediendo a vista principal del veterinario");
	    

	    // ✅ MODIFICADO: Si no hay usuario, redirigir al login
	    if (principal == null) {
	        System.out.println("❌ Usuario no autenticado - Redirigiendo al login");
	        return "redirect:/login";
	    }

	    String correo = principal.getName();
	    System.out.println("📧 Buscando perfil para: " + correo);

	    // Buscar usuario por correo
	    Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorCorreo(correo);
	    
	    if (usuarioOpt.isPresent()) {
	        Usuario usuario = usuarioOpt.get();
	        
	        // Verificar si es veterinario (rol_id = 2)
	        if (usuario.getRol() != null && usuario.getRol().getId() == 2) {
	            // Buscar perfil veterinario por usuario_id
	            Optional<PerfilVeterinario> perfilOpt = perfilVeterinarioService.buscarPorUsuarioId(usuario.getId());
	            
	            if (perfilOpt.isPresent()) {
	                model.addAttribute("perfilVeterinario", perfilOpt.get());
	                System.out.println("✅ Perfil veterinario encontrado para: " + correo);
	            } else {
	                // ERROR: Veterinario sin perfil (no debería pasar)
	                System.out.println("❌ ERROR: Veterinario sin perfil en BD: " + correo);
	                model.addAttribute("error", "Error: Perfil de veterinario no encontrado. Contacte al administrador.");
	                // Aún así cargamos las mascotas
	            }
	        } else {
	            System.out.println("❌ Usuario no tiene rol de veterinario");
	            model.addAttribute("error", "No tiene permisos de veterinario");
	            return "redirect:/acceso-denegado";
	        }
	    } else {
	        System.out.println("❌ Usuario no encontrado: " + correo);
	        model.addAttribute("error", "Usuario no encontrado");
	        return "redirect:/login";
	    }

	    // 🔹 MANTENIDO: Cargamos TODAS las mascotas de la BD
	    List<Mascota> mascotas = mascotaService.listarMascotas();
	    System.out.println("🐾 Mascotas encontradas: " + mascotas.size());
	    mascotas.forEach(m -> System.out.println(" - " + m.getId() + " | " + m.getNombre()));
	    model.addAttribute("mascotas", mascotas);

	    return "perfil-veterinario/perfil-veterinario";
	}

	// ==================== OTRAS SECCIONES (PLACEHOLDERS) ====================
	@GetMapping("/inicio")
	public String inicio() {
		return "perfil-veterinario/inicio";
	}

	@GetMapping("/agenda")
	public String agenda(Model model) {
		return "perfil-veterinario/agenda";
	}

	@GetMapping("/historias-clinicas")
	public String historiasClinicas() {
		return "perfil-veterinario/historias-clinicas";
	}

	@GetMapping("/adopciones")
	public String adopciones() {
		return "perfil-veterinario/adopciones";
	}

	@GetMapping("/pet-shop")
	public String petShop() {
		return "perfil-veterinario/pet-shop";
	}

	// ==================== SECCIÓN CONFIGURACIÓN ====================
	@GetMapping("/configuracion")
	public String mostrarConfiguracion(Principal principal, Model model) {
	    System.out.println("🔧 Accediendo a configuración");

	    if (principal == null) {
	        System.out.println("❌ Usuario no autenticado - Redirigiendo al login");
	        return "redirect:/login";
	    }

	    String correo = principal.getName();
	    System.out.println("📧 Buscando perfil para: " + correo);

	    // Buscar usuario por correo
	    Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorCorreo(correo);
	    
	    if (usuarioOpt.isPresent()) {
	        Usuario usuario = usuarioOpt.get();
	        
	        // Verificar si es veterinario (rol_id = 2)
	        if (usuario.getRol() != null && usuario.getRol().getId() == 2) {
	            // Buscar perfil veterinario por usuario_id - DEBE existir
	            Optional<PerfilVeterinario> perfilOpt = perfilVeterinarioService.buscarPorUsuarioId(usuario.getId());
	            
	            if (perfilOpt.isPresent()) {
	                PerfilVeterinario perfil = perfilOpt.get();
	                model.addAttribute("perfilVeterinario", perfil);
	                System.out.println("✅ Perfil veterinario encontrado para: " + correo);
	            } else {
	                // ERROR: Veterinario sin perfil (no debería pasar)
	                System.out.println("❌ ERROR: Veterinario sin perfil en BD: " + correo);
	                model.addAttribute("error", "Error: Perfil de veterinario no encontrado. Contacte al administrador.");
	            }
	        } else {
	            System.out.println("❌ Usuario no tiene rol de veterinario");
	            model.addAttribute("error", "No tiene permisos de veterinario");
	            return "redirect:/acceso-denegado";
	        }
	    } else {
	        System.out.println("❌ Usuario no encontrado: " + correo);
	        model.addAttribute("error", "Usuario no encontrado");
	        return "redirect:/login";
	    }

	    return "perfil-veterinario/configuracion";
	}

	// ==================== ACTUALIZAR CONFIGURACIÓN ====================
	@PostMapping("/configuracion/actualizar")
	public String actualizarConfiguracion(@ModelAttribute PerfilVeterinario perfilForm, 
	                                    Principal principal,
	                                    RedirectAttributes redirectAttributes) {
	    System.out.println("🔄 Procesando actualización de configuración");

	    if (principal == null) {
	        return "redirect:/login";
	    }

	    try {
	        String correo = principal.getName();
	        Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorCorreo(correo);
	        
	        if (usuarioOpt.isPresent() && usuarioOpt.get().getRol().getId() == 2) {
	            Usuario usuario = usuarioOpt.get();
	            
	            // Crear un nuevo objeto con los datos actualizados
	            Usuario usuarioActualizado = new Usuario();
	            usuarioActualizado.setId(usuario.getId()); // ✅ Mantener el mismo ID
	            usuarioActualizado.setNombres(perfilForm.getUsuario().getNombres());
	            usuarioActualizado.setApellidos(perfilForm.getUsuario().getApellidos());
	            usuarioActualizado.setTelefono(perfilForm.getUsuario().getTelefono());
	            usuarioActualizado.setCorreo(usuario.getCorreo()); // ✅ Mantener correo
	            usuarioActualizado.setRol(usuario.getRol()); // ✅ Mantener rol
	            // ... otros campos que necesites mantener
	            
	            // ✅ Usar TU método más seguro
	            Usuario usuarioGuardado = usuarioService.actualizarUsuario(usuario.getId(), usuarioActualizado);
	            
	            // Buscar perfil veterinario existente - DEBE existir
	            Optional<PerfilVeterinario> perfilExistenteOpt = perfilVeterinarioService.buscarPorUsuarioId(usuario.getId());
	            
	            if (perfilExistenteOpt.isPresent()) {
	                PerfilVeterinario perfilExistente = perfilExistenteOpt.get();
	                
	                // Actualizar perfil veterinario con TU método seguro
	                PerfilVeterinario perfilActualizado = new PerfilVeterinario();
	                perfilActualizado.setId(perfilExistente.getId());
	                perfilActualizado.setEspecialidad(perfilForm.getEspecialidad());
	                perfilActualizado.setExperiencia(perfilForm.getExperiencia());
	                perfilActualizado.setTarjetaProfesional(perfilForm.getTarjetaProfesional());
	                perfilActualizado.setUsuario(usuarioGuardado); // ✅ Usar el usuario actualizado
	                perfilActualizado.setEstado(perfilExistente.getEstado()); // ✅ Mantener estado
	                // ... otros campos necesarios
	                
	                PerfilVeterinario perfilGuardado = perfilVeterinarioService.actualizarPerfil(perfilExistente.getId(), perfilActualizado);
	                redirectAttributes.addFlashAttribute("success", "✅ Perfil actualizado correctamente");
	                System.out.println("✅ Perfil actualizado para: " + correo);
	            } else {
	                // ERROR: No debería pasar
	                System.out.println("❌ ERROR: Intentando actualizar perfil que no existe: " + correo);
	                redirectAttributes.addFlashAttribute("error", "❌ Error: Perfil no encontrado");
	            }
	        } else {
	            redirectAttributes.addFlashAttribute("error", "❌ No tiene permisos de veterinario");
	        }

	        return "redirect:/perfil-veterinario/configuracion";

	    } catch (Exception e) {
	        System.out.println("❌ Error al actualizar perfil: " + e.getMessage());
	        redirectAttributes.addFlashAttribute("error", "❌ Error al actualizar perfil: " + e.getMessage());
	        return "redirect:/perfil-veterinario/configuracion";
	    }
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// MODAL CITA!!!!!!!!!!!!!
	@GetMapping("/perfil-veterinario")
	public String nuevaCita(Model model) {

		List<Mascota> mascotas = mascotaService.listarMascotas();
		model.addAttribute("mascotas", mascotas);
		return "perfil-veterinario/perfil-veterinario"; // tu vista que tiene el modal
	}

	@PostMapping("/cita/guardar")
	public String guardarCita(@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
			@RequestParam("hora") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
			@RequestParam("motivo") String motivo, @RequestParam("mascota.id") Integer mascotaId) {

		Cita cita = new Cita();
		cita.setFechaHora(LocalDateTime.of(fecha, hora));
		cita.setMotivo(motivo);

		// Asignar la mascota
		Mascota mascota = mascotaService.buscarMascotaPorId(mascotaId)
				.orElseThrow(() -> new IllegalArgumentException("Mascota no encontrada con id: " + mascotaId));

		cita.setMascota(mascota);

		// Asignar el veterinario logueado
		PerfilVeterinario vet = obtenerVeterinarioLogueado(); // Método que devuelve el vet logueado
		cita.setVeterinario(vet);

		citaService.guardarCita(cita);

		return "perfil-veterinario/perfil-veterinario";
	}

	private PerfilVeterinario obtenerVeterinarioLogueado() {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuario == null) {
			throw new IllegalStateException("No hay usuario logueado");
		}
		return perfilVeterinarioService.buscarPorUsuarioId(usuario.getId())
				.orElseThrow(() -> new IllegalArgumentException("Perfil veterinario no encontrado"));
	}

	// MODAL DE AGREGAR PRODUCTO!!!!!!!!!!!!!!1
	@PostMapping("/producto/guardar")
	public String guardarProducto(@ModelAttribute Producto producto, @RequestParam("fileImagen") MultipartFile imagen,
			@RequestParam("cantidadDisponible") Integer cantidadDisponible,
			@RequestParam("idveterinaria") Integer idVeterinaria, Model model) {

		if (!imagen.isEmpty()) {
			try {
				String nombreArchivo = Paths.get(imagen.getOriginalFilename()).getFileName().toString();
				String rutaBase = "C:/imagenesProductos/";
				Path ruta = Paths.get(rutaBase + nombreArchivo);
				Files.createDirectories(ruta.getParent());
				imagen.transferTo(ruta.toFile());
				producto.setImagen(nombreArchivo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		productoService.crearProducto(producto); // Guarda en la base de datos
		List<Producto> productos = productoService.obtenerTodosLosProductos();
		model.addAttribute("productos", productos);

		Inventario inventario = inventarioService.obtenerInventarioPorVeterinariaYProducto(idVeterinaria,
				producto.getId());

		if (inventario != null) {
			inventario.agregarStock(cantidadDisponible); // suma si ya existeInteger veterinariaId, Integer productoId
		} else {
			inventario = new Inventario();
			inventario.setProducto(producto);
			Veterinaria veterinaria = veterinariaService.obtenerPorId(idVeterinaria)
					.orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));

			inventario.setVeterinaria(veterinaria);
			inventario.setCantidadDisponible(cantidadDisponible);
			inventario.actualizarEstado();
		}

		inventarioService.guardarInventario(inventario);

		return "redirect:/perfil-veterinario";
	}

	// MODAL EMERGENCIA!!!!!!!!!!!!!!!!
	@PostMapping("/emergencia/guardar")
	public String guardarEmergencia(@RequestParam("mascotaId") Integer mascotaId,
			@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
			@RequestParam("hora") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
			@RequestParam("tipo") String tipo, @RequestParam("descripcion") String descripcion,
			@RequestParam("veterinariaId") Integer veterinariaId, @RequestParam("veterinarioId") Integer veterinarioId,
			RedirectAttributes redirectAttributes) {

		Mascota mascota = mascotaService.buscarMascotaPorId(mascotaId)
				.orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

		Veterinaria veterinaria = veterinariaService.obtenerPorId(veterinariaId)
				.orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));

		PerfilVeterinario veterinario = perfilVeterinarioService.buscarPorId(veterinarioId)
				.orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));

		Emergencia emergencia = new Emergencia();
		emergencia.setMascota(mascota);
		emergencia.setVeterinaria(veterinaria);
		emergencia.setVeterinario(veterinario);
		emergencia.setTipo(tipo);
		emergencia.setDescripcion(descripcion);
		emergencia.setFechayhora(LocalDateTime.of(fecha, hora));

		emergenciaService.guardarEmergencia(emergencia);

		redirectAttributes.addFlashAttribute("mensaje", "Emergencia registrada con éxito");
		return "redirect:/perfil-veterinario";
	}

}