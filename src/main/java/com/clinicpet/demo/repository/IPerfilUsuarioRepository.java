package com.clinicpet.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.clinicpet.demo.model.PerfilUsuario;

@Repository
public interface IPerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Integer> {

	// Buscar perfil por ID de usuario
	PerfilUsuario findByUsuarioId(Integer usuarioId);

	// Buscar perfil por nombre de usuario
	PerfilUsuario findByUsuarioUsername(String username);

	// Buscar perfiles por dirección
	java.util.List<PerfilUsuario> findByDireccionContaining(String direccion);

	// Buscar perfiles con mascotas
	@Query("SELECT pu FROM PerfilUsuario pu WHERE SIZE(pu.mascota) > 0")
	java.util.List<PerfilUsuario> findPerfilesConMascotas();

	// Contar mascotas por perfil de usuario
	@Query("SELECT pu.id, COUNT(m) FROM PerfilUsuario pu LEFT JOIN pu.mascota m GROUP BY pu.id")
	java.util.List<Object[]> countMascotasPorPerfil();

	// Verificar si existe perfil para un usuario
	boolean existsByUsuarioId(Integer usuarioId);
}