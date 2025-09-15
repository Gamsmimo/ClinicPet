package com.clinicpet.demo.repository;

import com.clinicpet.demo.model.Veterinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IVeterinariaRepository extends JpaRepository<Veterinaria, Integer> {

	Optional<Veterinaria> findByNombre(String nombre);

	List<Veterinaria> findByNombreContainingIgnoreCase(String nombre);

	List<Veterinaria> findByDireccionContainingIgnoreCase(String direccion);

	Optional<Veterinaria> findByCorreo(String correo);

	Optional<Veterinaria> findByTelefono(String telefono);

	boolean existsByNombre(String nombre);

	boolean existsByCorreo(String correo);

	boolean existsByTelefono(String telefono);

	@Query("SELECT v FROM Veterinaria v WHERE LOWER(v.direccion) LIKE LOWER(CONCAT('%',:ciudad, '%'))")
	List<Veterinaria> findByCiudad(@Param("ciudad") String ciudad);

	// Este método ya viene por defecto con JpaRepository, puedes eliminarlo
	// long countTotalVeterinarias();

	// Corrige el nombre del campo "servicios" (debe coincidir con tu entidad)
	@Query("SELECT DISTINCT v FROM Veterinaria v JOIN v.servicios s WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :servicio, '%'))")
	List<Veterinaria> findByServicioNombre(@Param("servicio") String servicio);

	List<Veterinaria> findByHorarioContaining(String horario);

	List<Veterinaria> findAllByOrderByNombreAsc();

	List<Veterinaria> findByDescripcionContainingIgnoreCase(String descripcion);
}