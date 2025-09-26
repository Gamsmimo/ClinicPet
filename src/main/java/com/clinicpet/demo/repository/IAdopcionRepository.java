package com.clinicpet.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Adopcion;

@Repository
public interface IAdopcionRepository extends JpaRepository<Adopcion, Integer> {

	// Mascotas que están en adopción para vista usuario
	List<Adopcion> findByEstado(String estado);

	// Adopciones gestionadas por una veterinaria
	List<Adopcion> findByVeterinariaId(Integer idVeterinaria);

	// Adopciones realizadas por un perfil de usuario (adoptante)
	List<Adopcion> findByUsuarioAdoptanteId(Integer idPerfilUsuario);

	// adopcion de una mascota
	Optional<Adopcion> findByMascotaId(Integer idMascota);

}