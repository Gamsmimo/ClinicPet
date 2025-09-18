package com.clinicpet.demo.service;

import com.clinicpet.demo.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
	Usuario crearUsuario(Usuario usuario);

	Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado);

	List<Usuario> listarTodosUsuarios();

	Optional<Usuario> buscarUsuarioPorId(Integer id);

	Optional<Usuario> buscarUsuarioPorUsername(String username);

	Optional<Usuario> buscarUsuarioPorCorreo(String correo);

	Optional<Usuario> buscarUsuarioPorDocumento(String numDocumento);

	List<Usuario> buscarUsuariosPorRol(Integer rolId);

	List<Usuario> buscarUsuariosPorNombreOApellido(String nombre, String apellidos);

	boolean existeUsername(String username);

	boolean existeCorreo(String correo);

	boolean existeDocumento(String numDocumento);

	void eliminarUsuario(Integer id);

	boolean validarCredenciales(String username, String contraseña);
}