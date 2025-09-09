package com.clinicpet.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Veterinaria;

@Repository
public interface IVeterinariaRepository {
	Optional<Veterinaria> findByNombre(String nombre);

	List<Veterinaria> findByNombreContainingIgnoreCase(String nombre);

	List<Veterinaria> findByDireccionContainingIgnoreCase(String direccion);

	Optional<Veterinaria> finByCorreo(String correo);

	Optional<Veterinaria> finByTelefono(String telefono);

	boolean existByNombre(String nombre);

	boolean existByCorreo(String correo);

	boolean existByTelefono(String telefono);

	@Query("SELECT v FROM Veterinaria v WHERE LOWER(v.direccion) LIKE LOWER(CONCAT('%',:ciudad, '%'))")
	List<Veterinaria> findByCiudad(@Param("ciudad") String ciudad);

	@Query("SELECT COUNT(v) FROM Veterinaria v ")
	long countTotalVeterinarias();

	@Query("SELECT DISTINCT v FROM Veterinaria v JOIN v.serivicios s WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :servicio, '%'))")
	List<Veterinaria> findByServicioNombre(@Param("servicio") String servicio);

	List<Veterinaria> findByHorarioContaining(String horario);

	List<Veterinaria> findAllByOrdenByNombreAsc();

	List<Veterinaria> findByDescripcionContainingIgnoreCase(String descripcion);

}
