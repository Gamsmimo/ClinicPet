package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilAdministrador;
import java.util.List;
import java.util.Optional;

public interface IPerfilAdministradorService {

	PerfilAdministrador crearPerfilAdministrador(PerfilAdministrador perfilAdmin);

	Optional<PerfilAdministrador> obtenerPerfilAdministradorPorId(Integer id);

	PerfilAdministrador obtenerPerfilAdministradorPorUsuarioId(Integer usuarioId);

	PerfilAdministrador obtenerPerfilAdministradorPorUsername(String username);

	List<PerfilAdministrador> obtenerTodosLosPerfilesAdministradores();

	List<PerfilAdministrador> obtenerPerfilesPorPermisos(String permiso);

	PerfilAdministrador actualizarPerfilAdministrador(Integer id, PerfilAdministrador perfilAdmin);

	void eliminarPerfilAdministrador(Integer id);

	boolean existePerfilAdministradorPorUsuarioId(Integer usuarioId);

	boolean existePerfilAdministradorPorId(Integer id);
}