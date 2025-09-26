package com.clinicpet.demo.service;

<<<<<<< HEAD
import java.util.Optional;

import com.clinicpet.demo.model.PerfilVeterinario;

public interface IPerfilVeterinarioService {

	// Guardar/actualizar perfil
	PerfilVeterinario guardarPerfil(PerfilVeterinario perfil);

	// Buscar por ID
	Optional<PerfilVeterinario> buscarPorId(Integer id);

	// Buscar por ID de usuario
	Optional<PerfilVeterinario> buscarPorUsuarioId(Integer usuarioId);

	// Buscar por correo del usuario
	Optional<PerfilVeterinario> buscarPorUsuarioCorreo(String correo);

	// Buscar por documento del usuario
	Optional<PerfilVeterinario> buscarPorUsuarioNumDocumento(String numDocumento);

	// Actualizar información del perfil (sin afectar usuario)
	PerfilVeterinario actualizarPerfil(Integer id, PerfilVeterinario perfilActualizado);

	// Verificar existencia por correo
	boolean existePorUsuarioCorreo(String correo);

	// Verificar existencia por documento
	boolean existePorUsuarioNumDocumento(String numDocumento);
=======
import com.clinicpet.demo.model.PerfilVeterinario;

import java.util.List;
import java.util.Optional;

public interface IPerfilVeterinarioService {
	List<PerfilVeterinario> ListarPorEstado(String estado);

	PerfilVeterinario crearPerfilVeterinario(PerfilVeterinario perfilVeterinario);

	PerfilVeterinario findById(Integer id);

	List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinario();

	PerfilVeterinario obtenerPerfilVeterinarioPorUsuarioId(Integer usuarioId);

	PerfilVeterinario obtenerPerfilVeterinarioPorNombres(String nombres);

	List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinarios();

	List<PerfilVeterinario> obtenerVeterinariosPorEspecialidad(String especialidad);

	PerfilVeterinario obtenerVeterinarioPorTarjetaProfesional(String tarjetaProfesional);

	List<PerfilVeterinario> obtenerVeterinariosDisponibles();

	PerfilVeterinario actualizarPerfilVeterinario(Integer id, PerfilVeterinario perfilVeterinario);

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorTargetaProfesional(String tarjetaProfesional);

	void aprobarVeterinario(Integer id);

	PerfilVeterinario desactivarVeterinario(Integer id);

	PerfilVeterinario activarVeterinario(Integer id);

	void rechazarVeterinario(Integer id);

	void editarVeterinario(Integer id);
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8

}