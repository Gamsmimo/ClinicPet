package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilAdmin;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface IPerfilAdminService {

	Optional<PerfilAdmin> obtenerAdminPorId(Integer id);

	Optional<PerfilAdmin> obtenerAdminPrincipal(); // si hay solo un admin

	void actualizarAdmin(PerfilAdmin admin);

	void actualizarFoto(Integer id, MultipartFile foto);
}
