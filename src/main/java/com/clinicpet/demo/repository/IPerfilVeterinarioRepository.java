package com.clinicpet.demo.repository;

<<<<<<< HEAD
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.model.Usuario;
=======
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.clinicpet.demo.model.PerfilVeterinario;
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8

@Repository
public interface IPerfilVeterinarioRepository extends JpaRepository<PerfilVeterinario, Integer> {

<<<<<<< HEAD
	// Buscar perfil por ID de usuario
	Optional<PerfilVeterinario> findByUsuarioId(Integer usuarioId);

	// Buscar perfil por el objeto de usuario, correo y documento
	Optional<PerfilVeterinario> findByUsuario(Usuario usuario);

	Optional<PerfilVeterinario> findByUsuarioCorreo(String correo);

	Optional<PerfilVeterinario> findByUsuarioNumDocumento(String numDocumento);

	// Verificar existencia por email
	boolean existsByUsuarioCorreo(String correo);

	boolean existsByUsuarioNumDocumento(String numDocumento);
=======
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
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8
}