package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilUsuario;
import java.util.List;
import java.util.Optional;

public interface IPerfilUsuarioService {
	PerfilUsuario crearPerfilUsuario(PerfilUsuario perfilUsuario);

	Optional<PerfilUsuario> obtenerPerfilUsuarioPorId(Integer id);

	List<PerfilUsuario> obtenerTodosLosPerfilesUsuario();

	PerfilUsuario actualizarPerfilUsuario(Integer id, PerfilUsuario perfilUsuario);

	void eliminarPerfilUsuario(Integer id);

	Optional<PerfilUsuario> obtenerPerfilUsuarioPorUsuarioId(Integer usuarioId);

	Optional<PerfilUsuario> obtenerPerfilUsuarioPorUsername(String username);

	List<PerfilUsuario> obtenerPerfilesUsuarioPorDireccion(String direccion);

	List<PerfilUsuario> obtenerPerfilesUsuarioPorTelefono(String telefono);

	List<PerfilUsuario> obtenerPerfilesUsuarioConMascotas();

	boolean existePerfilUsuarioPorUsuarioId(Integer usuarioId);
}