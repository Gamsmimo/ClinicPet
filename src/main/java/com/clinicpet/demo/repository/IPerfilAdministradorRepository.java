package com.clinicpet.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.clinicpet.demo.model.PerfilAdministrador;

@Repository
public interface IPerfilAdministradorRepository extends JpaRepository<PerfilAdministrador, Integer> {

	// Buscar perfil por ID de usuario
	PerfilAdministrador findByUsuarioId(Integer usuarioId);

	// Buscar perfil por nombre de usuario
	PerfilAdministrador findByUsuarioUsername(String username);

	// Verificar si existe perfil para un usuario
	boolean existsByUsuarioId(Integer usuarioId);

	// Buscar por permisos específicos
	java.util.List<PerfilAdministrador> findByPermisosContaining(String permiso);
}
