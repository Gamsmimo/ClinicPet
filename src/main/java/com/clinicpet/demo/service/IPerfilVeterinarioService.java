package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilVeterinario;
import java.util.List;
import java.util.Optional;

public interface IPerfilVeterinarioService {

	PerfilVeterinario crearPerfilVeterinario(PerfilVeterinario perfilVeterinario);

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorId(Integer id);

	PerfilVeterinario obtenerPerfilVeterinarioPorUsuarioId(Integer usuarioId);

	PerfilVeterinario obtenerPerfilVeterinarioPorUsername(String username);

	List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinarios();

	List<PerfilVeterinario> obtenerVeterinariosPorEspecialidad(String especialidad);

	PerfilVeterinario obtenerVeterinarioPorRut(String rut);

	List<PerfilVeterinario> obtenerVeterinariosPorTelefono(String telefono);

	List<PerfilVeterinario> obtenerVeterinariosDisponibles();

	PerfilVeterinario actualizarPerfilVeterinario(Integer id, PerfilVeterinario perfilVeterinario);

	void eliminarPerfilVeterinario(Integer id);

	boolean existePerfilVeterinarioPorUsuarioId(Integer usuarioId);

	boolean existePerfilVeterinarioPorId(Integer id);

	boolean existeVeterinarioPorRut(String rut);

	long contarCitasPorVeterinario(Integer veterinarioId);
}