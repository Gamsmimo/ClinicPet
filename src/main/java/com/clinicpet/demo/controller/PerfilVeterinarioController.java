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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	public String mostrarPerfilVeterinario(HttpSession session, Model model,
	        @RequestParam(required = false) String categoria,
	        @RequestParam(required = false) String estado) {
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
	    Veterinaria veterinaria = null;
	    Integer veterinariaId = null;

	    if (perfilOpt.isPresent()) {
	        PerfilVeterinario perfil = perfilOpt.get();
	        model.addAttribute("perfilVeterinario", perfil);
	        veterinaria = perfil.getVeterinaria();
	        if (veterinaria != null) {
	            veterinariaId = veterinaria.getId();
	            model.addAttribute("veterinaria", veterinaria);
	            System.out.println("✅ Perfil veterinario encontrado para: " + correo + " - Veterinaria ID: " + veterinariaId);
	        } else {
	            System.out.println("⚠️ Perfil veterinario sin veterinaria asociada");
	        }
	    } else {
	        System.out.println("❌ ERROR: Veterinario sin perfil en BD: " + correo);
	        model.addAttribute("error", "Error: Perfil de veterinario no encontrado.");
	    }

	    // 🔹 Cargar mascotas (MANTENIDO)
	    List<Mascota> mascotas = mascotaService.listarMascotas();
	    System.out.println("🐾 Mascotas encontradas: " + mascotas.size());
	    model.addAttribute("mascotas", mascotas);

	    // 🔹 🔹 🔹 CARGAR PRODUCTOS PARA PET SHOP FILTRADOS POR VETERINARIA 🔹 🔹 🔹
	    try {
	        System.out.println("🛍️ Cargando productos para Pet Shop...");

	        if (veterinariaId == null) {
	            System.out.println("⚠️ No se encontró veterinaria asociada al perfil. No se cargarán productos.");
	            model.addAttribute("productos", new ArrayList<Producto>());
	            model.addAttribute("inventarioPorProducto", new HashMap<Integer, Inventario>());
	        } else {
	            List<Producto> productos = productoService.obtenerTodosLosProductos();
	            System.out.println("📦 Productos encontrados: " + productos.size());
	            
	            // Obtener inventario SOLO de la veterinaria del perfil
	            List<Inventario> inventarios = inventarioService.obtenerInventarioPorVeterinaria(veterinariaId);
	            System.out.println("📊 Registros de inventario para veterinaria " + veterinariaId + ": " + inventarios.size());
	            
	            // Crear mapa de inventario
	            Map<Integer, Inventario> inventarioPorProducto = new HashMap<>();
	            for (Inventario inventario : inventarios) {
	                if (inventario.getProducto() != null) {
	                    inventarioPorProducto.put(inventario.getProducto().getId(), inventario);
	                }
	            }
	            
	            // Aplicar filtros de categoría y estado si vienen en la petición
	            List<Producto> productosFiltrados = new ArrayList<>();
	            for (Producto p : productos) {
	                // SOLO considerar productos que tengan inventario en esta veterinaria
	                if (!inventarioPorProducto.containsKey(p.getId())) {
	                    continue;
	                }
	
	                boolean coincide = true;
	                
	                if (categoria != null && !categoria.isEmpty() && !"Todas las categorías".equalsIgnoreCase(categoria)) {
	                    if (p.getCategoria() == null || !p.getCategoria().equalsIgnoreCase(categoria)) {
	                        coincide = false;
	                    }
	                }
	                
	                if (coincide && estado != null && !estado.isEmpty() && !"Todos".equalsIgnoreCase(estado)) {
	                    Inventario inv = inventarioPorProducto.get(p.getId());
	                    String estadoInv = (inv != null && inv.getEstado() != null) ? inv.getEstado() : "agotado";
	                    
	                    if ("Disponible".equalsIgnoreCase(estado) && !"disponible".equalsIgnoreCase(estadoInv)) {
	                        coincide = false;
	                    } else if ("Agotado".equalsIgnoreCase(estado) && "disponible".equalsIgnoreCase(estadoInv)) {
	                        coincide = false;
	                    }
	                }
	                
	                if (coincide) {
	                    productosFiltrados.add(p);
	                }
	            }
	            
	            model.addAttribute("productos", productosFiltrados);
	            model.addAttribute("inventarioPorProducto", inventarioPorProducto);
	            model.addAttribute("categoriaSeleccionada", categoria);
	            model.addAttribute("estadoSeleccionado", estado);
	            System.out.println("✅ Pet Shop cargado correctamente con filtros aplicados para veterinaria " + veterinariaId);
	        }
	        
	    } catch (Exception e) {
	        System.out.println("❌ Error cargando Pet Shop: " + e.getMessage());
	        model.addAttribute("productos", new ArrayList<>());
	        model.addAttribute("inventarioPorProducto", new HashMap<>());
	    }

	    return "perfil-veterinario/perfil-veterinario";
	}
	
	@GetMapping("/productos/filtrar")
	@ResponseBody
	public Map<String, Object> filtrarProductos(
	        @RequestParam(required = false) String categoria,
	        @RequestParam(required = false) String estado) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        System.out.println("🔍 Filtro AJAX - categoria: " + categoria + ", estado: " + estado);
	        
	        // Obtener usuario y perfil para conocer la veterinaria
	        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
	        if (usuarioLogueado == null) {
	            response.put("error", "Usuario no autenticado");
	            return response;
	        }

	        Optional<PerfilVeterinario> perfilOpt = perfilVeterinarioService.buscarPorUsuarioId(usuarioLogueado.getId());
	        if (perfilOpt.isEmpty() || perfilOpt.get().getVeterinaria() == null) {
	            response.put("error", "Perfil de veterinario o veterinaria no encontrados");
	            return response;
	        }

	        Integer veterinariaId = perfilOpt.get().getVeterinaria().getId();
	        System.out.println("🔍 Filtro AJAX para veterinaria ID: " + veterinariaId);

	        // Productos solo de esta veterinaria (a partir del inventario)
	        List<Inventario> inventarios = inventarioService.obtenerInventarioPorVeterinaria(veterinariaId);
	        Map<Integer, Inventario> inventarioPorProducto = new HashMap<>();
	        List<Producto> productos = productoService.obtenerTodosLosProductos();

	        for (Inventario inventario : inventarios) {
	        	if (inventario.getProducto() != null) {
	        		Integer prodId = inventario.getProducto().getId();
	        		inventarioPorProducto.put(prodId, inventario);
	        	}
	        }
	        
	        List<Map<String, Object>> listaFiltrada = new ArrayList<>();
	        for (Producto p : productos) {
	        	// SOLO considerar productos que tengan inventario en esta veterinaria
	            if (!inventarioPorProducto.containsKey(p.getId())) {
	                continue;
	            }
	        
	            boolean coincide = true;
	            
	            if (categoria != null && !categoria.isEmpty() && !"Todas las categorías".equalsIgnoreCase(categoria)) {
	                if (p.getCategoria() == null || !p.getCategoria().equalsIgnoreCase(categoria)) {
	                    coincide = false;
	                }
	            }
	            
	            Inventario inv = inventarioPorProducto.get(p.getId());
	            String estadoInv = (inv != null && inv.getEstado() != null) ? inv.getEstado() : "agotado";
	            
	            if (coincide && estado != null && !estado.isEmpty() && !"Todos".equalsIgnoreCase(estado)) {
	                if ("Disponible".equalsIgnoreCase(estado) && !"disponible".equalsIgnoreCase(estadoInv)) {
	                    coincide = false;
	                } else if ("Agotado".equalsIgnoreCase(estado) && "disponible".equalsIgnoreCase(estadoInv)) {
	                    coincide = false;
	                }
	            }
	            
	            if (coincide) {
	                Map<String, Object> item = new HashMap<>();
	                item.put("id", p.getId());
	                item.put("nombre", p.getNombre());
	                item.put("descripcion", p.getDescripcion());
	                item.put("precio", p.getPrecio());
	                item.put("categoria", p.getCategoria());
	                item.put("imagen", p.getImagen());
	                item.put("cantidadDisponible", inv != null ? inv.getCantidadDisponible() : 0);
	                item.put("estado", estadoInv);
	                listaFiltrada.add(item);
	            }
	        }
	        
	        response.put("productos", listaFiltrada);
	        System.out.println("✅ Filtro AJAX - productos retornados: " + listaFiltrada.size());
	    } catch (Exception e) {
	        System.err.println("💥 Error en filtro AJAX: " + e.getMessage());
	        response.put("error", e.getMessage());
	    }
	    return response;
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
				    if (fotoFile.getSize() > 10 * 1024 * 1024) {
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

	

	// MODAL DE AGREGAR PRODUCTO!!!!!!!!!!!!!!
	@PostMapping("/producto/guardar")
	public String guardarProducto(
	        @RequestParam("nombre") String nombre,
	        @RequestParam("precio") Double precio,
	        @RequestParam("categoria") String categoria,
	        @RequestParam("descripcion") String descripcion,
	        @RequestParam("fileImagen") MultipartFile imagen,
	        @RequestParam("cantidadDisponible") Integer cantidadDisponible,
	        @RequestParam("idveterinaria") Integer idVeterinaria, // Hacerlo REQUERIDO
	        Model model) {

	    try {
	        System.out.println("=== INICIANDO GUARDAR PRODUCTO ===");
	        System.out.println("🔍 ID Veterinaria recibido: " + idVeterinaria);
	        
	        // VALIDACIÓN BÁSICA
	        if (idVeterinaria == null || idVeterinaria <= 0) {
	            throw new RuntimeException("ID de veterinaria inválido: " + idVeterinaria);
	        }
	        
	        if (cantidadDisponible == null || cantidadDisponible < 0) {
	            cantidadDisponible = 0;
	        }

	        System.out.println("📝 Datos recibidos - Veterinaria: " + idVeterinaria + ", Cantidad: " + cantidadDisponible);

	      
	        
	        // 1. Crear y guardar el producto
	        
	        System.out.println("🔍 Buscando producto existente...");

	        Producto productoGuardado;
	        Optional<Producto> productoExistente = productoService.buscarPorNombreYCategoria(nombre.trim(), categoria);

	        if (productoExistente.isPresent()) {
	            // ✅ PRODUCTO EXISTENTE - ACTUALIZARLO con los nuevos datos
	            productoGuardado = productoExistente.get();
	            System.out.println("📦 Producto existente encontrado, ID: " + productoGuardado.getId());
	            
	            // ACTUALIZAR los campos que pueden cambiar
	            productoGuardado.setPrecio(precio); // ✅ Actualizar precio
	            productoGuardado.setDescripcion(descripcion != null ? descripcion.trim() : ""); // ✅ Actualizar descripción
	            
	            // ACTUALIZAR imagen solo si se subió una nueva
	            if (imagen != null && !imagen.isEmpty()) {
	                try {
	                    String uploadsDir = System.getProperty("user.dir") + "/uploads/";
	                    String nombreOriginal = imagen.getOriginalFilename();
	                    String extension = nombreOriginal.contains(".") ? 
	                        nombreOriginal.substring(nombreOriginal.lastIndexOf(".")) : "";
	                    
	                    String nombreArchivo = System.currentTimeMillis() + "_" + 
	                        (nombre.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase()) + extension;
	                    
	                    Path rutaCompleta = Paths.get(uploadsDir + nombreArchivo);
	                    Files.createDirectories(rutaCompleta.getParent());
	                    imagen.transferTo(rutaCompleta.toFile());
	                    
	                    productoGuardado.setImagen("/uploads/" + nombreArchivo); // ✅ Actualizar imagen
	                    System.out.println("🖼️ Nueva imagen guardada: " + productoGuardado.getImagen());
	                    
	                } catch (IOException e) {
	                    System.err.println("❌ Error al guardar nueva imagen: " + e.getMessage());
	                }
	            } else {
	                System.out.println("📷 No se subió nueva imagen, se mantiene la anterior");
	            }
	            
	            // Guardar los cambios del producto actualizado
	            productoGuardado = productoService.actualizarProducto(productoGuardado);
	            System.out.println("🔄 Producto actualizado - Precio: " + productoGuardado.getPrecio());
	            
	        } else {
	            // 🆕 PRODUCTO NUEVO - Crearlo (tu código actual)
	            System.out.println("🆕 Creando nuevo producto...");
	            Producto producto = new Producto();
	            producto.setNombre(nombre.trim());
	            producto.setPrecio(precio);
	            producto.setCategoria(categoria);
	            producto.setDescripcion(descripcion != null ? descripcion.trim() : "");

	            // Guardar imagen (tu código actual)
	            if (imagen != null && !imagen.isEmpty()) {
	                try {
	                    String uploadsDir = System.getProperty("user.dir") + "/uploads/";
	                    String nombreOriginal = imagen.getOriginalFilename();
	                    String extension = nombreOriginal.contains(".") ? 
	                        nombreOriginal.substring(nombreOriginal.lastIndexOf(".")) : "";
	                    
	                    String nombreArchivo = System.currentTimeMillis() + "_" + 
	                        (nombre.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase()) + extension;
	                    
	                    Path rutaCompleta = Paths.get(uploadsDir + nombreArchivo);
	                    Files.createDirectories(rutaCompleta.getParent());
	                    imagen.transferTo(rutaCompleta.toFile());
	                    
	                    producto.setImagen("/uploads/" + nombreArchivo);
	                    System.out.println("🖼️ Imagen guardada: " + producto.getImagen());
	                    
	                } catch (IOException e) {
	                    System.err.println("❌ Error al guardar imagen: " + e.getMessage());
	                }
	            }

	            productoGuardado = productoService.crearProducto(producto);
	            System.out.println("✅ Nuevo producto guardado con ID: " + productoGuardado.getId());
	        }

	        // 2. Obtener veterinaria de la base de datos (tu código actual)
	        System.out.println("🔍 Buscando veterinaria en BD con ID: " + idVeterinaria);
	        Veterinaria veterinaria = veterinariaService.obtenerPorId(idVeterinaria)
	                .orElseThrow(() -> new RuntimeException("Veterinaria no encontrada en BD con ID: " + idVeterinaria));
	        System.out.println("✅ Veterinaria encontrada: " + veterinaria.getNombre());

	        // 3. GESTIÓN DE INVENTARIO (tu código actual corregido)
	        System.out.println("🔄 Procesando inventario...");

	     // Buscar inventario existente para ESTE producto en ESTA veterinaria
	     Inventario inventarioExistente = inventarioService.obtenerInventarioPorVeterinariaYProducto(
	             idVeterinaria, productoGuardado.getId());

	     Inventario inventario;

	     if (inventarioExistente != null) {
	         System.out.println("📦 Inventario existente encontrado, ID: " + inventarioExistente.getId());
	         System.out.println("📦 Producto: " + inventarioExistente.getProducto().getNombre());
	         System.out.println("📦 Cantidad actual: " + inventarioExistente.getCantidadDisponible());
	         System.out.println("📦 Nueva cantidad a agregar: " + cantidadDisponible);
	         
	         // ✅ USAR EL MÉTODO DEL SERVICE PARA AGREGAR STOCK
	         inventario = inventarioService.agregarStock(inventarioExistente.getId(), cantidadDisponible);
	         System.out.println("🔄 Stock actualizado - Nueva cantidad: " + inventario.getCantidadDisponible());
	     } else {
	         System.out.println("🆕 Creando NUEVO registro de inventario...");
	         
	         inventario = new Inventario();
	         inventario.setProducto(productoGuardado);
	         inventario.setVeterinaria(veterinaria);
	         inventario.setCantidadDisponible(cantidadDisponible);
	         inventario.setFechaActualizacion(LocalDate.now());
	         inventario.actualizarEstado();
	         
	         System.out.println("📊 Antes de guardar - Producto ID: " + inventario.getProducto().getId());
	         System.out.println("📊 Antes de guardar - Veterinaria ID: " + inventario.getVeterinaria().getId());
	         System.out.println("📊 Antes de guardar - Cantidad: " + inventario.getCantidadDisponible());
	         
	         try {
	             Inventario inventarioGuardado = inventarioService.guardarInventario(inventario);
	             
	             if (inventarioGuardado != null && inventarioGuardado.getId() != null) {
	                 System.out.println("✅ NUEVO inventario guardado con ID: " + inventarioGuardado.getId());
	                 inventario = inventarioGuardado;
	             } else {
	                 throw new RuntimeException("El inventario no se guardó correctamente - ID es null");
	             }
	             
	         } catch (Exception e) {
	             System.err.println("💥 ERROR CRÍTICO al guardar inventario: " + e.getMessage());
	             e.printStackTrace();
	             throw new RuntimeException("Error al guardar en inventario: " + e.getMessage(), e);
	         }
	     }

	        // 4. VERIFICACIÓN FINAL
	        System.out.println("=== VERIFICACIÓN FINAL ===");
	        System.out.println("🔍 Producto ID: " + productoGuardado.getId());
	        System.out.println("🔍 Veterinaria ID: " + idVeterinaria);
	        System.out.println("🔍 Inventario ID: " + inventario.getId());
	        System.out.println("🔍 Cantidad en inventario: " + inventario.getCantidadDisponible());
	        System.out.println("🔍 Estado: " + inventario.getEstado());

	        System.out.println("🎉 PROCESO COMPLETADO EXITOSAMENTE");
	        return "redirect:/perfil-veterinario?success=true";

	    } catch (Exception e) {
	        System.err.println("💥 ERROR CRÍTICO al guardar producto: " + e.getMessage());
	        e.printStackTrace();
	        return "redirect:/perfil-veterinario?error=" + e.getMessage();
	    }
	}

	
	@GetMapping("/producto/datos/{idProducto}")
	@ResponseBody
	public Map<String, Object> obtenerDatosProducto(@PathVariable Integer idProducto) {
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        System.out.println("🔍 Obteniendo datos del producto ID: " + idProducto);
	        
	        Optional<Producto> productoOpt = productoService.obtenerProductoPorId(idProducto);
	        if (productoOpt.isPresent()) {
	            Producto producto = productoOpt.get();
	            
	            // Buscar inventario (usa el idVeterinaria que necesites, aquí uso 1 como ejemplo)
	            Inventario inventario = inventarioService.obtenerInventarioPorVeterinariaYProducto(1, idProducto);
	            
	            response.put("id", producto.getId());
	            response.put("nombre", producto.getNombre());
	            response.put("descripcion", producto.getDescripcion());
	            response.put("precio", producto.getPrecio());
	            response.put("categoria", producto.getCategoria());
	            response.put("imagen", producto.getImagen());
	            response.put("cantidadDisponible", inventario != null ? inventario.getCantidadDisponible() : 0);
	            
	            System.out.println("✅ Datos cargados para producto: " + producto.getNombre());
	        } else {
	            response.put("error", "Producto no encontrado");
	            System.err.println("❌ Producto no encontrado ID: " + idProducto);
	        }
	    } catch (Exception e) {
	        response.put("error", "Error: " + e.getMessage());
	        System.err.println("💥 Error al obtener datos del producto: " + e.getMessage());
	    }
	    
	    return response;
	}
	

	@PostMapping("/producto/actualizar/{idProducto}")
	public String actualizarProducto(
	        @PathVariable Integer idProducto,
	        @RequestParam String nombre,
	        @RequestParam Double precio,
	        @RequestParam(required = false) String categoria,
	        @RequestParam String descripcion,
	        @RequestParam Integer cantidadDisponible,
	        @RequestParam(required = false) MultipartFile imagen,
	        @RequestParam Integer idveterinaria) {
	    
	    try {
	        System.out.println("🔄 Actualizando producto ID: " + idProducto);
	        
	        // Verificar que el producto existe
	        Optional<Producto> productoExistenteOpt = productoService.obtenerProductoPorId(idProducto);
	        if (productoExistenteOpt.isEmpty()) {
	            throw new RuntimeException("Producto no encontrado con ID: " + idProducto);
	        }
	        
	        Producto producto = productoExistenteOpt.get();
	        
	        // Actualizar SOLO los campos permitidos
	        // Nombre y categoría NO se tocan para mantenerlos inmutables
	        producto.setPrecio(precio);
	        producto.setDescripcion(descripcion != null ? descripcion.trim() : "");
	        
	        // Actualizar imagen solo si se subió una nueva
	        if (imagen != null && !imagen.isEmpty()) {
	            try {
	                String uploadsDir = System.getProperty("user.dir") + "/uploads/";
	                String nombreOriginal = imagen.getOriginalFilename();
	                String extension = nombreOriginal.contains(".") ? 
	                    nombreOriginal.substring(nombreOriginal.lastIndexOf(".")) : "";
	                
	                String nombreArchivo = System.currentTimeMillis() + "_" + 
	                    (nombre.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase()) + extension;
	                
	                Path rutaCompleta = Paths.get(uploadsDir + nombreArchivo);
	                Files.createDirectories(rutaCompleta.getParent());
	                imagen.transferTo(rutaCompleta.toFile());
	                
	                producto.setImagen("/uploads/" + nombreArchivo);
	                System.out.println("🖼️ Nueva imagen guardada: " + producto.getImagen());
	                
	            } catch (IOException e) {
	                System.err.println("❌ Error al guardar nueva imagen: " + e.getMessage());
	            }
	        }
	        
	        // Guardar producto actualizado
	        Producto productoActualizado = productoService.actualizarProducto(producto);
	        System.out.println("✅ Producto actualizado: " + productoActualizado.getNombre());
	        
	        // Actualizar inventario
	        Inventario inventario = inventarioService.obtenerInventarioPorVeterinariaYProducto(idveterinaria, idProducto);
	        if (inventario != null) {
	            inventario.setCantidadDisponible(cantidadDisponible);
	            inventario.setFechaActualizacion(LocalDate.now());
	            inventario.actualizarEstado();
	            inventarioService.guardarInventario(inventario);
	            System.out.println("📦 Inventario actualizado - Cantidad: " + cantidadDisponible);
	        } else {
	            // Crear nuevo registro de inventario si no existe
	            Veterinaria veterinaria = veterinariaService.obtenerPorId(idveterinaria)
	                    .orElseThrow(() -> new RuntimeException("Veterinaria no encontrada"));
	            
	            Inventario nuevoInventario = new Inventario();
	            nuevoInventario.setProducto(productoActualizado);
	            nuevoInventario.setVeterinaria(veterinaria);
	            nuevoInventario.setCantidadDisponible(cantidadDisponible);
	            nuevoInventario.setFechaActualizacion(LocalDate.now());
	            nuevoInventario.actualizarEstado();
	            
	            inventarioService.guardarInventario(nuevoInventario);
	            System.out.println("📦 Nuevo inventario creado - Cantidad: " + cantidadDisponible);
	        }
	        
	        System.out.println("🎉 Producto actualizado exitosamente");
	        return "redirect:/perfil-veterinario?success=Producto actualizado correctamente";
	        
	    } catch (Exception e) {
	        System.err.println("💥 Error al actualizar producto: " + e.getMessage());
	        e.printStackTrace();
	        return "redirect:/perfil-veterinario?error=" + e.getMessage();
	    }
	}
	

	@PostMapping("/producto/eliminar/{idProducto}")
	public String eliminarProducto(@PathVariable Integer idProducto, RedirectAttributes redirectAttributes) {
	    try {
	        System.out.println("🗑️ Eliminando producto ID: " + idProducto);
	        
	        // Eliminar registros de inventario asociados al producto
	        List<Inventario> inventarios = inventarioService.obtenerInventarioPorProducto(idProducto);
	        for (Inventario inv : inventarios) {
	            System.out.println("🗑️ Eliminando inventario ID: " + inv.getId());
	            inventarioService.eliminarInventario(inv.getId());
	        }
	        
	        // Eliminar el producto
	        productoService.eliminarProducto(idProducto);
	        System.out.println("✅ Producto eliminado correctamente");
	        redirectAttributes.addFlashAttribute("success", "Producto eliminado correctamente");
	    } catch (Exception e) {
	        System.err.println("💥 Error al eliminar producto: " + e.getMessage());
	        redirectAttributes.addFlashAttribute("error", "Error al eliminar producto: " + e.getMessage());
	    }
	    
	    return "redirect:/perfil-veterinario";
	}
	
	
}