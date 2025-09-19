package com.clinicpet.demo.repository;

import com.clinicpet.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	// Buscar por nombres, correo o documento
	Optional<Usuario> findByNombres(String nombres);

	Optional<Usuario> findByCorreo(String correo);

	Optional<Usuario> findByNumDocumento(String numDocumento);

	// Buscar por nombre o apellido
	List<Usuario> findByNombresContainingOrApellidosContaining(String nombres, String apellidos);

	// Verificar existencia
	boolean existsByNombres(String nombres);

	boolean existsByCorreo(String correo);

	boolean existsByNumDocumento(String numDocumento);

	// Buscar por rol
	List<Usuario> findByRolId(Integer rolId);

	// Buscar por dirección
	List<Usuario> findByDireccionContaining(String direccion);

	// Buscar usuarios que tengan al menos 1 mascota
	List<Usuario> findByMascotasIsNotEmpty();

}
