package com.clinicpet.demo.controller;

import com.clinicpet.demo.model.Adopcion;
import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.service.IAdopcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/adopciones")
public class AdopcionController {

	@Autowired
	private IAdopcionService adopcionService;

	// Mostrar página principal de adopciones
	@GetMapping
	public String listarMascotasEnAdopcion(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "9") int size,
			@SessionAttribute(value = "usuarioLogueado", required = false) Usuario usuarioLogueado, Model model) {

		if (usuarioLogueado == null) {
			return "redirect:/usuarios/iniciarsesion";
		}

		// Obtener todas las mascotas disponibles de OTROS usuarios
		Pageable pageable = PageRequest.of(page, size, Sort.by("fechaPublicacion").descending());
		Page<Adopcion> todasLasAdopciones = adopcionService.buscarDisponibles(pageable);

		// Filtrar para excluir las del usuario actual
		Page<Adopcion> adopcionesDeOtros = todasLasAdopciones;

		// Obtener las mascotas publicadas por el usuario actual
		List<Adopcion> misPublicaciones = adopcionService.buscarAdopcionesByUsuarioId(usuarioLogueado.getId());

		model.addAttribute("adopciones", adopcionesDeOtros);
		model.addAttribute("misPublicaciones", misPublicaciones);
		model.addAttribute("usuarioLogueado", usuarioLogueado);

		// Inicializar objeto vacío para el formulario
		if (!model.containsAttribute("nuevaAdopcion")) {
			model.addAttribute("nuevaAdopcion", new Adopcion());
		}

		return "Adopcion/adopcion";
	}

	// Publicar una nueva mascota en adopción
	@PostMapping("/publicar")
	public String publicarMascota(@ModelAttribute("nuevaAdopcion") Adopcion adopcion,
			@RequestParam(value = "archivoImagen", required = true) MultipartFile archivoImagen,
			@SessionAttribute("usuarioLogueado") Usuario usuarioLogueado, RedirectAttributes redirectAttributes) {

		System.out.println("\n=== INICIANDO PUBLICACIÓN DE MASCOTA ===");
		System.out.println("Usuario: " + usuarioLogueado.getNombres());
		System.out.println("Nombre mascota: " + adopcion.getNombreMascota());

		try {
			// Validar campos obligatorios
			if (adopcion.getNombreMascota() == null || adopcion.getNombreMascota().trim().isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "El nombre de la mascota es obligatorio");
				return "redirect:/adopciones";
			}

			if (adopcion.getTipoMascota() == null || adopcion.getTipoMascota().trim().isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "El tipo de mascota es obligatorio");
				return "redirect:/adopciones";
			}

			// Validar y guardar imagen (OBLIGATORIA)
			if (archivoImagen == null || archivoImagen.isEmpty()) {
				System.out.println("ERROR: No se recibió imagen");
				redirectAttributes.addFlashAttribute("error", "La imagen es obligatoria");
				return "redirect:/adopciones";
			}

			System.out.println("Imagen recibida: " + archivoImagen.getOriginalFilename());
			System.out.println("Tamaño: " + archivoImagen.getSize());

			// Validar tipo de archivo
			String contentType = archivoImagen.getContentType();
			System.out.println("Content-Type: " + contentType);

			if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/jpg")
					&& !contentType.equals("image/png"))) {
				redirectAttributes.addFlashAttribute("error", "Solo se permiten imágenes JPG, JPEG o PNG");
				return "redirect:/adopciones";
			}

			// Validar tamaño (5MB)
			if (archivoImagen.getSize() > 5 * 1024 * 1024) {
				redirectAttributes.addFlashAttribute("error", "La imagen no debe superar los 5MB");
				return "redirect:/adopciones";
			}

			// Guardar imagen
			System.out.println("Guardando imagen...");
			String nombreArchivo = guardarImagen(archivoImagen);
			adopcion.setImagen(nombreArchivo);
			System.out.println("Imagen guardada: " + nombreArchivo);

			// Configurar la adopción
			adopcion.setUsuario(usuarioLogueado);
			adopcion.setEstado(Adopcion.ESTADO_DISPONIBLE);
			adopcion.setFechaPublicacion(new Date());
			adopcion.setMascota(null);

			// Guardar en la base de datos
			System.out.println("Guardando en base de datos...");
			Adopcion adopcionGuardada = adopcionService.guardarAdopcion(adopcion);
			System.out.println("Adopción guardada con ID: " + adopcionGuardada.getId());
			System.out.println("=== PUBLICACIÓN EXITOSA ===\n");

			redirectAttributes.addFlashAttribute("success",
					"¡Mascota " + adopcion.getNombreMascota() + " publicada exitosamente para adopción!");
			return "redirect:/adopciones";

		} catch (IOException e) {
			System.err.println("ERROR DE IO: " + e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error al guardar la imagen: " + e.getMessage());
			return "redirect:/adopciones";
		} catch (Exception e) {
			System.err.println("ERROR GENERAL: " + e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error al publicar la mascota: " + e.getMessage());
			return "redirect:/adopciones";
		}
	}

	// Ver detalles de una mascota (AJAX - devuelve JSON)
	@GetMapping("/{id}/detalles")
	@ResponseBody
	public Adopcion verDetalleMascota(@PathVariable Integer id,
			@SessionAttribute(value = "usuarioLogueado", required = false) Usuario usuarioLogueado) {

		if (usuarioLogueado == null) {
			throw new RuntimeException("Usuario no autenticado");
		}

		return adopcionService.buscarAdopcionById(id).orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
	}

	// Cambiar estado de una adopción (solo el dueño puede hacerlo)
	@PostMapping("/{id}/cambiar-estado")
	public String cambiarEstadoAdopcion(@PathVariable Integer id, @RequestParam String nuevoEstado,
			@SessionAttribute("usuarioLogueado") Usuario usuarioLogueado, RedirectAttributes redirectAttributes) {

		try {
			Adopcion adopcion = adopcionService.buscarAdopcionById(id)
					.orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

			// Verificar que el usuario sea el dueño de la publicación
			if (!adopcion.getUsuario().getId().equals(usuarioLogueado.getId())) {
				redirectAttributes.addFlashAttribute("error",
						"No tienes permiso para cambiar el estado de esta mascota");
				return "redirect:/adopciones";
			}

			// Validar y aplicar el cambio de estado
			switch (nuevoEstado) {
			case Adopcion.ESTADO_EN_PROCESO:
				if (adopcion.estaDisponible()) {
					adopcion.setEstado(Adopcion.ESTADO_EN_PROCESO);
				} else {
					redirectAttributes.addFlashAttribute("error",
							"Solo se puede marcar como 'En Proceso' si está disponible");
					return "redirect:/adopciones";
				}
				break;

			case Adopcion.ESTADO_ADOPTADO:
				adopcion.setEstado(Adopcion.ESTADO_ADOPTADO);
				break;

			case Adopcion.ESTADO_DISPONIBLE:
				adopcion.setEstado(Adopcion.ESTADO_DISPONIBLE);
				break;

			default:
				redirectAttributes.addFlashAttribute("error", "Estado no válido");
				return "redirect:/adopciones";
			}

			adopcionService.guardarAdopcion(adopcion);
			redirectAttributes.addFlashAttribute("success",
					"Estado actualizado a: " + obtenerNombreEstado(nuevoEstado));
			return "redirect:/adopciones";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al cambiar el estado: " + e.getMessage());
			return "redirect:/adopciones";
		}
	}

	// Eliminar una publicación de adopción
	@PostMapping("/{id}/eliminar")
	public String eliminarAdopcion(@PathVariable Integer id,
			@SessionAttribute("usuarioLogueado") Usuario usuarioLogueado, RedirectAttributes redirectAttributes) {

		try {
			Adopcion adopcion = adopcionService.buscarAdopcionById(id)
					.orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

			// Verificar que el usuario sea el dueño
			if (!adopcion.getUsuario().getId().equals(usuarioLogueado.getId())) {
				redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar esta publicación");
				return "redirect:/adopciones";
			}

			// Eliminar imagen si no es la predeterminada
			if (adopcion.getImagen() != null && !adopcion.getImagen().equals("default.jpg")) {
				eliminarImagen(adopcion.getImagen());
			}

			adopcionService.eliminarAdopcion(id);
			redirectAttributes.addFlashAttribute("success", "Publicación eliminada correctamente");
			return "redirect:/adopciones";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al eliminar la publicación: " + e.getMessage());
			return "redirect:/adopciones";
		}
	}

	// Método auxiliar para guardar imágenes
	private String guardarImagen(MultipartFile archivo) throws IOException {
		try {
			String uploadDir = System.getProperty("user.dir") + "/uploads/";

			System.out.println("=== GUARDANDO IMAGEN ===");
			System.out.println("Directorio: " + uploadDir);
			System.out.println("Archivo original: " + archivo.getOriginalFilename());
			System.out.println("Tamaño: " + archivo.getSize() + " bytes");
			System.out.println("Tipo: " + archivo.getContentType());

			// Crear directorio si no existe
			File directorio = new File(uploadDir);
			if (!directorio.exists()) {
				boolean created = directorio.mkdirs();
				System.out.println("Directorio creado: " + created);
			}

			// Generar nombre único
			String nombreOriginal = archivo.getOriginalFilename();
			String extension = "";
			if (nombreOriginal != null && nombreOriginal.contains(".")) {
				extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
			} else {
				extension = ".jpg";
			}

			String nombreArchivo = UUID.randomUUID().toString() + extension;
			System.out.println("Nombre generado: " + nombreArchivo);

			// Guardar archivo
			Path rutaCompleta = Paths.get(uploadDir + nombreArchivo);
			Files.copy(archivo.getInputStream(), rutaCompleta, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

			System.out.println("Archivo guardado en: " + rutaCompleta.toString());
			System.out.println("Archivo existe: " + rutaCompleta.toFile().exists());
			System.out.println("=== IMAGEN GUARDADA EXITOSAMENTE ===");

			return nombreArchivo;

		} catch (Exception e) {
			System.err.println("ERROR al guardar imagen: " + e.getMessage());
			e.printStackTrace();
			throw new IOException("Error al guardar la imagen: " + e.getMessage());
		}
	}

	// Método auxiliar para eliminar imágenes
	private void eliminarImagen(String nombreArchivo) {
		try {
			String uploadDir = System.getProperty("user.dir") + "/uploads/";
			Path rutaArchivo = Paths.get(uploadDir + nombreArchivo);
			Files.deleteIfExists(rutaArchivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Método auxiliar para nombres de estados
	private String obtenerNombreEstado(String estado) {
		switch (estado) {
		case Adopcion.ESTADO_DISPONIBLE:
			return "En espera";
		case Adopcion.ESTADO_EN_PROCESO:
			return "En Proceso";
		case Adopcion.ESTADO_ADOPTADO:
			return "Adoptado";
		default:
			return estado;
		}
	}
}