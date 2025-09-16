package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilVeterinario;
import java.util.List;
import java.util.Optional;

public interface IPerfilVeterinarioService {

	PerfilVeterinario crearPerfilVeterinario(PerfilVeterinario perfilVeterinario);

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorId(Integer id);

	List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinario();

	PerfilVeterinario obtenerPerfilVeterinarioPorUsuarioId(Integer usuarioId);

	PerfilVeterinario obtenerPerfilVeterinarioPorUsername(String username);

	List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinarios();

	List<PerfilVeterinario> obtenerVeterinariosPorEspecialidad(String especialidad);

	PerfilVeterinario obtenerVeterinarioPorRut(String rut);

	List<PerfilVeterinario> obtenerVeterinariosPorTelefono(String telefono);

	List<PerfilVeterinario> obtenerVeterinariosDisponibles();

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorRut(String rut);

	PerfilVeterinario actualizarPerfilVeterinario(Integer id, PerfilVeterinario perfilVeterinario);

	void eliminarPerfilVeterinario(Integer id);
}