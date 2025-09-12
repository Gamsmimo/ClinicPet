package com.clinicpet.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.clinicpet.demo.model.PerfilVeterinario;

@Repository
public interface IPerfilVeterinarioRepository extends JpaRepository<PerfilVeterinario, Integer> {

	// Buscar perfil por ID de usuario
	PerfilVeterinario findByUsuarioId(Integer usuarioId);

	// Buscar perfil por nombre de usuario
	PerfilVeterinario findByUsuarioUsername(String username);

	// Buscar veterinarios por especialidad
	java.util.List<PerfilVeterinario> findByEspecialidad(String especialidad);

	// Buscar veterinarios por RUT
	PerfilVeterinario findByRut(String rut);

	// Buscar veterinarios por teléfono
	java.util.List<PerfilVeterinario> findByTelefono(String telefono);

	// Buscar veterinarios disponibles (sin emergencias activas)
	@Query("SELECT pv FROM PerfilVeterinario pv WHERE SIZE(pv.emergencia) = 0")
	java.util.List<PerfilVeterinario> findVeterinariosDisponibles();

	// Contar citas por veterinario
	@Query("SELECT pv.id, COUNT(c) FROM PerfilVeterinario pv LEFT JOIN pv.cita c GROUP BY pv.id")
	java.util.List<Object[]> countCitasPorVeterinario();

	// Verificar si existe perfil para un usuario
	boolean existsByUsuarioId(Integer usuarioId);

	// Verificar si existe RUT
	boolean existsByRut(String rut);
}