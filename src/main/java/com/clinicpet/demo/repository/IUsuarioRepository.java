package com.clinicpet.demo.repository;

import com.clinicpet.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	// Solo mantén los métodos que coincidan con los campos de tu entidad
	Optional<Usuario> findByUsername(String username);

	Optional<Usuario> findByCorreo(String correo);

	Optional<Usuario> findByNumDocumento(String numDocumento);

	List<Usuario> findByNombreContainingOrApellidosContaining(String nombre, String apellidos);

	// Métodos de existencia
	boolean existsByUsername(String username);

	boolean existsByCorreo(String correo);

	boolean existsByNumDocumento(String numDocumento);

	// Si necesitas buscar por rol, agrega este método
	List<Usuario> findByRolId(Integer rolId);
}