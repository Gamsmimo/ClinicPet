package com.clinicpet.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.model.VeterinariaVeterinario;

import jakarta.transaction.Transactional;

@Repository
public interface IVeterinariaVeterinarioRepository extends JpaRepository<VeterinariaVeterinario, Integer> {

	@Query("SELECT vv.veterinaria FROM Veterinariaveterinario vv WHERE vv.veterinario.id = :veterinarioId ")
	List<Usuario> findVeterinariosByVeterinarioId(@Param("veterianrioId") Integer veterinarioId);

	@Query("SELECT vv.veterinaria FROM Veterinariaveterinario vv WHERE vv.veterinario.id = :veterinarioId ")
	List<Usuario> findVeterinariosByVeterinariaId(@Param("veterinariaId") Integer veterinariaId);

	Optional<VeterinariaVeterinario> findByVeterinariaIdVeterinarioId(Integer veterinariaId, Integer veterinarioId);

	boolean existsByVeterinariaIdAndVeterinarioId(Integer veterinariaId, Integer veterinarioId);

	long countByVeterinariaId(Integer veterinariaId);

	long countByVeterinarioId(Integer veterinarioId);

	@Transactional
	@Modifying
	void deleteByVeterinaria(Integer veterinariaId);

	@Transactional
	@Modifying
	void deleteByVeterinario(Integer veterinarioId);

	@Query("SELECT vv.veterinaria FROM VeterinariaVeterinario vv WHERE vv.veterinario.id = :veterinarioId ")
	List<Integer> findVeterinariaIdsByVeterinarioId(@Param("veterinarioId") Integer veterinarioId);

	@Query("SELECT vv.veterinario FROM VeterinariaVeterinario vv WHERE vv.veterinaria.id = :veterinariaId ")
	List<Integer> findVeterinarioIdsByVeterinariaId(@Param("veterinariaId") Integer veterinariaId);

}
