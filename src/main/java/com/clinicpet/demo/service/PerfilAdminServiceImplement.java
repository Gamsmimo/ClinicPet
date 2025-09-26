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

	private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/"; // carpeta donde se guardarán las

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

	// ACTUALIZAR FOTO
	@Override
	public void actualizarFoto(Integer id, MultipartFile foto) {
		if (!foto.isEmpty()) {
			try {
				String uploadDir = "uploads/"; // carpeta donde se guarda la foto

				PerfilAdmin admin = obtenerAdminPorId(id).orElse(null);
				if (admin != null) {

					// Borrar foto anterior si existe
					if (admin.getFoto() != null && !admin.getFoto().isEmpty()) {
						File archivoAnterior = new File(uploadDir + admin.getFoto());
						if (archivoAnterior.exists()) {
							boolean borrado = archivoAnterior.delete();
							if (!borrado) {
								System.out.println("No se pudo borrar la foto anterior: " + archivoAnterior.getName());
							}
						}
					}

					// Guardar nueva foto
					String nombreArchivo = System.currentTimeMillis() + "_" + foto.getOriginalFilename();
					Path ruta = Paths.get(uploadDir + nombreArchivo);
					Files.write(ruta, foto.getBytes());

					admin.setFoto(nombreArchivo);
					adminRepository.save(admin);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
