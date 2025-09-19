package com.clinicpet.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.clinicpet.demo.model.PerfilVeterinario;

@Repository
public interface IPerfilVeterinarioRepository extends JpaRepository<PerfilVeterinario, Integer> {

	PerfilVeterinario findByUsuarioId(Integer usuarioId);

	List<PerfilVeterinario> findByEstado(String estado);

	PerfilVeterinario findByUsuarioNombres(String nombres);

	java.util.List<PerfilVeterinario> findByEspecialidad(String especialidad);

	PerfilVeterinario findBytarjetaProfesional(String tarjetaProfesional);

	@Query("SELECT pv FROM PerfilVeterinario pv WHERE SIZE(pv.emergencia) = 0")
	java.util.List<PerfilVeterinario> findVeterinariosDisponibles();

	@Query("SELECT pv.id, COUNT(c) FROM PerfilVeterinario pv LEFT JOIN pv.cita c GROUP BY pv.id")
	java.util.List<Object[]> countCitasPorVeterinario();

	boolean existsByUsuarioId(Integer usuarioId);

	boolean existsBytarjetaProfesional(String tarjetaProfesional);
}