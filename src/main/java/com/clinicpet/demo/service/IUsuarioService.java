package com.clinicpet.demo.service;

import com.clinicpet.demo.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

	Usuario crearUsuario(Usuario usuario);

	Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado);

	List<Usuario> listarTodosUsuarios();

	Optional<Usuario> buscarUsuarioPorId(Integer id);

	Optional<Usuario> buscarUsuarioPorNombres(String nombres);

	Optional<Usuario> buscarUsuarioPorCorreo(String correo);

	Optional<Usuario> buscarUsuarioPorDocumento(String numDocumento);

	List<Usuario> buscarUsuariosPorRol(Integer rolId);

	List<Usuario> buscarUsuariosPorNombresOApellidos(String nombres, String apellidos);

	boolean existeCorreo(String correo);

	boolean existeDocumento(String numDocumento);

	boolean existeNombres(String nombres);

	// NUEVOS MÉTODOS PARA ADMIN
	Usuario activarUsuario(Integer id);

	Usuario desactivarUsuario(Integer id);

	Usuario cambiarRolUsuario(Integer id, Integer rolId);

	void eliminarUsuario(Integer id);

	// LOGGIN

	boolean validarCredenciales(String nombres, String contraseña);

}
