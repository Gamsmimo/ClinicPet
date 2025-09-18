package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilUsuario;
import java.util.List;
import java.util.Optional;

public interface IPerfilUsuarioService {

	PerfilUsuario crearPerfilUsuario(PerfilUsuario perfilUsuario);

	Optional<PerfilUsuario> obtenerPerfilUsuarioPorId(Integer id);

	PerfilUsuario obtenerPerfilUsuarioPorUsuarioId(Integer usuarioId);

	PerfilUsuario obtenerPerfilUsuarioPorUsername(String username);

	List<PerfilUsuario> obtenerTodosLosPerfilesUsuarios();

	List<PerfilUsuario> obtenerPerfilesPorDireccion(String direccion);

	List<PerfilUsuario> obtenerPerfilesConMascotas();

	PerfilUsuario actualizarPerfilUsuario(Integer id, PerfilUsuario perfilUsuario);

	void eliminarPerfilUsuario(Integer id);

	boolean existePerfilUsuarioPorUsuarioId(Integer usuarioId);

	boolean existePerfilUsuarioPorId(Integer id);

	long contarMascotasPorPerfil(Integer perfilId);
}