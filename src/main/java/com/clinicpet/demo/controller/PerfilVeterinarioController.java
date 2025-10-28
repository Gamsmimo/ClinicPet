package com.clinicpet.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
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
	public String mostrarPerfilVeterinario(HttpSession session, Model model) {
		System.out.println("🔍 Accediendo a vista principal del veterinario");

		// ✅ USAR SESIÓN
		Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuarioLogueado == null) {
			System.out.println("❌ Usuario no autenticado - Redirigiendo al login");
			return "redirect:/usuarios/iniciarsesion";
		}

		// Verificar si es veterinario
		if (usuarioLogueado.getRol().getId() != 2) {
			System.out.println("❌ Usuario no tiene rol de veterinario");
			return "redirect:/acceso-denegado";
		}

		String correo = usuarioLogueado.getCorreo();
		System.out.println("📧 Buscando perfil para: " + correo);

		// Buscar perfil veterinario
		Optional<PerfilVeterinario> perfilOpt = perfilVeterinarioService.buscarPorUsuarioId(usuarioLogueado.getId());

		if (perfilOpt.isPresent()) {
			model.addAttribute("perfilVeterinario", perfilOpt.get());
			System.out.println("✅ Perfil veterinario encontrado para: " + correo);
		} else {
			System.out.println("❌ ERROR: Veterinario sin perfil en BD: " + correo);
			model.addAttribute("error", "Error: Perfil de veterinario no encontrado.");
		}

		// 🔹 Cargar mascotas (MANTENIDO)
		List<Mascota> mascotas = mascotaService.listarMascotas();
		System.out.println("🐾 Mascotas encontradas: " + mascotas.size());
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

	// ==================== SECCION CONFIGURACION ====================

	@PostMapping("/configuracion/actualizar")
	public String actualizarConfiguracion(@ModelAttribute PerfilVeterinario perfilForm,
			@RequestParam(value = "foto", required = false) MultipartFile fotoFile, HttpSession session,
			RedirectAttributes redirectAttributes) {
		System.out.println("🔄 Procesando actualización de configuración");

		// ✅ USAR SESIÓN
		Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuarioLogueado == null) {
			System.out.println("❌ Usuario no autenticado - Redirigiendo al login");
			return "redirect:/usuarios/iniciarsesion";
		}

		try {
			// Verificar si es veterinario
			if (usuarioLogueado.getRol().getId() != 2) {
				redirectAttributes.addFlashAttribute("error", "❌ No tiene permisos de veterinario");
				return "redirect:/perfil-veterinario";
			}

			// Buscar perfil veterinario existente
			Optional<PerfilVeterinario> perfilExistenteOpt = perfilVeterinarioService
					.buscarPorUsuarioId(usuarioLogueado.getId());

			if (perfilExistenteOpt.isPresent()) {
				PerfilVeterinario perfilExistente = perfilExistenteOpt.get();

				// ✅ ACTUALIZAR USUARIO EXISTENTE
				Usuario usuarioActual = usuarioLogueado;
				usuarioActual.setNombres(perfilForm.getUsuario().getNombres());
				usuarioActual.setApellidos(perfilForm.getUsuario().getApellidos());
				usuarioActual.setTelefono(perfilForm.getUsuario().getTelefono());
				usuarioActual.setDireccion(perfilForm.getUsuario().getDireccion());
				usuarioActual.setImagen(perfilForm.getUsuario().getImagen());	
				

				// 🔥 **MANEJO DE FOTO - CORREGIDO**
				// En tu método del controller, reemplaza esta parte:
				if (fotoFile != null && !fotoFile.isEmpty()) {
				    // Validaciones de foto
				    if (fotoFile.getSize() > 2 * 1024 * 1024) {
				        redirectAttributes.addFlashAttribute("error", "La imagen no debe superar 2MB");
				        return "redirect:/perfil-veterinario";
				    }

				    String contentType = fotoFile.getContentType();
				    if (contentType == null
				            || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
				        redirectAttributes.addFlashAttribute("error", "Formato no válido. Solo JPG y PNG");
				        return "redirect:/perfil-veterinario";
				    }

				    // CON LOS LOGS AÑADIDOS QUEDARÍA ASÍ:
				    System.out.println("=== DEBUG INICIO SUBIDA DE IMAGEN ===");
				    System.out.println("📸 Archivo recibido: " + fotoFile.getOriginalFilename());
				    System.out.println("📸 Tamaño archivo: " + fotoFile.getSize() + " bytes");
				    System.out.println("📸 ContentType: " + contentType);
				    System.out.println("📸 ¿Está vacío?: " + fotoFile.isEmpty());
				    
				    // Usar sistema uploads
				    String uploadDir = System.getProperty("user.dir") + "/uploads/";
				    System.out.println("📁 Ruta uploads: " + uploadDir);
				    
				    // Verificar si el directorio existe y tiene permisos
				    File dir = new File(uploadDir);
				    System.out.println("📁 ¿Directorio existe?: " + dir.exists());
				    System.out.println("📁 ¿Directorio puede escribir?: " + dir.canWrite());
				    System.out.println("📁 Ruta absoluta: " + dir.getAbsolutePath());
				    
				    String extension = contentType.equals("image/jpeg") ? ".jpg" : ".png";
				    String fileName = "vet_" + usuarioLogueado.getId() + "_" + System.currentTimeMillis() + extension;
				    System.out.println("📝 Nombre archivo generado: " + fileName);

				    Path uploadPath = Paths.get(uploadDir);
				    if (!Files.exists(uploadPath)) {
				        System.out.println("📁 Creando directorio...");
				        Files.createDirectories(uploadPath);
				    }

				    // Guardar foto
				    Path filePath = uploadPath.resolve(fileName);
				    System.out.println("💾 Ruta completa archivo: " + filePath.toString());
				    
				    try {
				        Files.copy(fotoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				        System.out.println("✅ Archivo guardado exitosamente");
				        
				        // Verificar si el archivo se creó
				        File savedFile = new File(filePath.toString());
				        System.out.println("✅ ¿Archivo guardado existe?: " + savedFile.exists());
				        System.out.println("✅ Tamaño archivo guardado: " + savedFile.length() + " bytes");
				        
				    } catch (IOException e) {
				        System.out.println("❌ Error al guardar archivo: " + e.getMessage());
				        e.printStackTrace();
				        throw e; // re-lanzar la excepción
				    }

				    // 🔥 **IMPORTANTE: Actualizar la imagen en el usuario**
				    usuarioActual.setImagen("/uploads/" + fileName);
				    usuarioService.actualizarUsuario(usuarioActual.getId(), usuarioActual);

				    System.out.println("👤 Nueva ruta de imagen en usuario: " + usuarioActual.getImagen());
				    
				    // AÑADE ESTO PARA VER SI SE GUARDA EN BD:
				    System.out.println("💾 Guardando usuario en BD...");
				    System.out.println("✅ Usuario actualizado en BD");
				    
				    System.out.println("=== DEBUG FIN SUBIDA DE IMAGEN ===");
				} else {
				    System.out.println("⚠️ No se recibió archivo de foto o está vacío");
				    System.out.println("⚠️ fotoFile es null: " + (fotoFile == null));
				    if (fotoFile != null) {
				        System.out.println("⚠️ fotoFile isEmpty: " + fotoFile.isEmpty());
				    }
				}
				

				Usuario usuarioGuardado = usuarioService.actualizarUsuario(usuarioActual.getId(), usuarioActual);

				// ✅ ACTUALIZAR PERFIL EXISTENTE
				perfilExistente.setEspecialidad(perfilForm.getEspecialidad());
				perfilExistente.setExperiencia(perfilForm.getExperiencia());
				perfilExistente.setTarjetaProfesional(perfilForm.getTarjetaProfesional());

				PerfilVeterinario perfilGuardado = perfilVeterinarioService.actualizarPerfil(perfilExistente.getId(),
						perfilExistente);

				// ✅ ACTUALIZAR SESIÓN con los nuevos datos
				session.setAttribute("usuarioLogueado", usuarioGuardado);

				redirectAttributes.addFlashAttribute("success", "✅ Perfil actualizado correctamente");
				System.out.println("✅ Perfil actualizado para: " + usuarioLogueado.getCorreo());
			} else {
				System.out
						.println("❌ ERROR: Intentando actualizar perfil que no existe: " + usuarioLogueado.getCorreo());
				redirectAttributes.addFlashAttribute("error", "❌ Error: Perfil no encontrado");
			}

			return "redirect:/perfil-veterinario";

		} catch (Exception e) {
			System.out.println("❌ Error al actualizar perfil: " + e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "❌ Error al actualizar perfil: " + e.getMessage());
			return "redirect:/perfil-veterinario";
		}
	}
	
	
	
	
	
	
	

    @PostMapping("/change-password")
    public String changePassword(
        @RequestParam String currentPassword,
        @RequestParam String newPassword,
        @RequestParam String confirmPassword,
        HttpSession session,
        Model model) {
        
        System.out.println("🎯 changePassword en VeterinarioController ejecutado!");
        
        try {
            Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
            if (usuarioLogueado == null) {
                model.addAttribute("error", "Debe iniciar sesión");
                return "redirect:/usuarios/iniciarsesion"; // ← CORREGIDO
            }
            
            System.out.println("🔐 Contraseña actual en BD: " + usuarioLogueado.getPassword());
            System.out.println("🔐 Contraseña ingresada: " + currentPassword);
            
            // Verificar contraseña actual
            if (!usuarioLogueado.getPassword().equals(currentPassword)) {
                model.addAttribute("error", "La contraseña actual es incorrecta");
                return "perfil-veterinario";
            }
            
            // Verificar que coincidan
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "Las nuevas contraseñas no coinciden");
                return "perfil-veterinario";
            }
            
            // Validar que la nueva contraseña sea diferente
            if (currentPassword.equals(newPassword)) {
                model.addAttribute("error", "La nueva contraseña debe ser diferente a la actual");
                return "perfil-veterinario";
            }
            
            System.out.println("✅ Contraseñas válidas, actualizando...");
            
            // Actualizar en BD
            usuarioService.actualizarPassword(usuarioLogueado.getId(), newPassword);
            
            // Actualizar sesión
            usuarioLogueado.setPassword(newPassword);
            session.setAttribute("usuario", usuarioLogueado);
            
            System.out.println("✅ Contraseña actualizada correctamente para usuario: " + usuarioLogueado.getCorreo());
            
            // Cerrar sesión después de cambiar contraseña
            session.invalidate();
            model.addAttribute("success", "Contraseña actualizada correctamente. Por favor inicie sesión nuevamente.");
            return "redirect:/usuarios/iniciarsesion"; // ← REDIRIGE AL LOGIN
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            model.addAttribute("error", "Error al cambiar la contraseña: " + e.getMessage());
            e.printStackTrace();
            return "perfil-veterinario";
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
	public String guardarProducto(@ModelAttribute Producto producto, 
	                             @RequestParam("fileImagen") MultipartFile imagen,
	                             @RequestParam("cantidadDisponible") Integer cantidadDisponible,
	                             @RequestParam("idveterinaria") Integer idVeterinaria, 
	                             Model model) {

	    // Validaciones básicas
	    if (cantidadDisponible == null || cantidadDisponible < 0) {
	        cantidadDisponible = 0;
	    }

	    // 1. Guardar la imagen
	    if (!imagen.isEmpty()) {
	        try {
	            // Usar la ruta configurada en tu clase ConfigUploads
	            String uploadsDir = System.getProperty("user.dir") + "/uploads/";
	            String nombreArchivo = System.currentTimeMillis() + "_" + 
	                Paths.get(imagen.getOriginalFilename()).getFileName().toString();
	            
	            Path rutaCompleta = Paths.get(uploadsDir + nombreArchivo);
	            Files.createDirectories(rutaCompleta.getParent());
	            imagen.transferTo(rutaCompleta.toFile());
	            
	            producto.setImagen("/uploads/" + nombreArchivo); // Ruta relativa para acceso web
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Puedes manejar el error como prefieras
	            producto.setImagen(null);
	        }
	    }

	    // 2. Guardar el producto PRIMERO para obtener el ID
	    Producto productoGuardado = productoService.crearProducto(producto);
	    
	    // Verificar que el producto se guardó correctamente
	    if (productoGuardado == null || productoGuardado.getId() == null) {
	        throw new RuntimeException("Error al guardar el producto");
	    }

	    // 3. Buscar la veterinaria
	    Veterinaria veterinaria = veterinariaService.obtenerPorId(idVeterinaria)
	            .orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));

	    // 4. Verificar si ya existe inventario para este producto y veterinaria
	    Inventario inventario = inventarioService.obtenerInventarioPorVeterinariaYProducto(
	            idVeterinaria, productoGuardado.getId());

	    if (inventario != null) {
	        // Si existe, actualizar el stock
	        inventario.agregarStock(cantidadDisponible);
	    } else {
	        // Si no existe, crear nuevo registro de inventario
	        inventario = new Inventario();
	        inventario.setProducto(productoGuardado);
	        inventario.setVeterinaria(veterinaria);
	        inventario.setCantidadDisponible(cantidadDisponible);
	        inventario.setFechaActualizacion(LocalDate.now()); // Fecha actual
	        inventario.actualizarEstado(); // Asumiendo que este método setea el estado según cantidad
	    }

	    // 5. Guardar el inventario
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