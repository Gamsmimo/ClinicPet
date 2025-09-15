package com.clinicpet.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
	Optional<Usuario> findByUsername(String username);

	Optional<Usuario> findByEmail(String email);

	Optional<Usuario> findByNombre(String nombre);

	Optional<Usuario> findByApellido(String apellido);

	Optional<Usuario> findByNombreAndApellido(String nombre, String apellido);

	Optional<Usuario> findByrolId(Integer rolId);

	@Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))")
	List<Usuario> buscarPorNombreOApellido(@Param("texto") String texto);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	Optional<Usuario> findByUsernameOrEmail(String username, String email);

}
