package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilAdmin;
import com.clinicpet.demo.repository.IPerfilAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class PerfilAdminServiceImplement implements IPerfilAdminService {

	@Autowired
	private IPerfilAdminRepository adminRepository;

	private final String UPLOAD_DIR = "src/main/resources/static/assets/uploads/"; // carpeta donde se guardarán las

	private PerfilAdmin admin;
	// fotos

	@Override
	public Optional<PerfilAdmin> obtenerAdminPorId(Integer id) {
		return adminRepository.findById(id);
	}

	@Override
	public Optional<PerfilAdmin> obtenerAdminPrincipal() {
		return adminRepository.findById(1); // se fija en 1 xq solo hay un admin
	}

	@Override
	public void actualizarAdmin(PerfilAdmin admin) {
		adminRepository.save(admin);
	}

	@Override
	public void actualizarFoto(Integer id, MultipartFile foto) {
		if (!foto.isEmpty()) {
			try {
				// Carpeta de uploads
				String uploadDir = "src/main/resources/static/assets/uploads/";

				// Obtener admin de la DB
				Optional<PerfilAdmin> optAdmin = obtenerAdminPorId(id);
				if (optAdmin.isPresent()) {
					PerfilAdmin admin = optAdmin.get();

					// Si ya tiene una foto, eliminarla
					if (admin.getFoto() != null) {
						File archivoAnterior = new File(uploadDir + admin.getFoto());
						if (archivoAnterior.exists()) {
							archivoAnterior.delete();
						}
					}

					// Generar un nombre único para la nueva foto
					String nombreArchivo = System.currentTimeMillis() + "_" + foto.getOriginalFilename();

					// Guardar físicamente la nueva imagen
					Path ruta = Paths.get(uploadDir + nombreArchivo);
					Files.write(ruta, foto.getBytes());

					// Guardar nombre en BD
					admin.setFoto(nombreArchivo);
					adminRepository.save(admin);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
