package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilVeterinario;
import java.util.List;
import java.util.Optional;

public interface IPerfilVeterinarioService {
<<<<<<< HEAD
=======

>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa
	PerfilVeterinario crearPerfilVeterinario(PerfilVeterinario perfilVeterinario);

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorId(Integer id);

<<<<<<< HEAD
	List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinario();
=======
	PerfilVeterinario obtenerPerfilVeterinarioPorUsuarioId(Integer usuarioId);

	PerfilVeterinario obtenerPerfilVeterinarioPorUsername(String username);

	List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinarios();

	List<PerfilVeterinario> obtenerVeterinariosPorEspecialidad(String especialidad);

	PerfilVeterinario obtenerVeterinarioPorRut(String rut);

	List<PerfilVeterinario> obtenerVeterinariosPorTelefono(String telefono);

	List<PerfilVeterinario> obtenerVeterinariosDisponibles();
>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa

	PerfilVeterinario actualizarPerfilVeterinario(Integer id, PerfilVeterinario perfilVeterinario);

	void eliminarPerfilVeterinario(Integer id);

<<<<<<< HEAD
	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorUsuarioId(Integer usuarioId);

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorUsername(String username);

	List<PerfilVeterinario> obtenerPerfilesVeterinarioPorEspecialidad(String especialidad);

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorRut(String rut);

	List<PerfilVeterinario> obtenerPerfilesVeterinarioPorTelefono(String telefono);

	List<PerfilVeterinario> obtenerVeterinariosDisponibles();

	boolean existePerfilVeterinarioPorUsuarioId(Integer usuarioId);

	boolean existePerfilVeterinarioPorRut(String rut);
=======
	boolean existePerfilVeterinarioPorUsuarioId(Integer usuarioId);

	boolean existePerfilVeterinarioPorId(Integer id);

	boolean existeVeterinarioPorRut(String rut);

	long contarCitasPorVeterinario(Integer veterinarioId);
>>>>>>> c9d59e5e8cb93bbecee88b06b84aecf01d2d3eaa
}